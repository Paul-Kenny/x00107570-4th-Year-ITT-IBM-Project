import java.awt.Desktop
import java.text.SimpleDateFormat

/**
 * Created by Paul on 3/25/17.
 */
class ScanImage {

    // Temporary directories and image name
    String tarballDir, untarDir, jarDir, imageName

    // Array to store all image layer tarballs
    def tarballArray = []
    // Array to store all jars found in image
    static jarList = []
    // Array to store all jar security vulnerabilities found
    static vulList = []

    // Constructor
    ScanImage(String imageNameIn, String tempDir) {

        // Temporary directories object
        def mkDir = new DirectoryOperations()

        // Make temporary directories
        tarballDir = mkDir.makeDir(tempDir)
        untarDir = mkDir.makeDir(tarballDir)
        jarDir = mkDir.makeDir(untarDir)
        imageName = imageNameIn
    }

    // Start the scan process
    void scanDockerImage() {

        // Create TarballOperations object
        def tarball = new TarballOperations(tarballDir, untarDir, jarDir, imageName)

        // Save the docker image tarball to the tarballDir
        def error = tarball.dockerTar()

        // Check if docker image exists
        if(error.isEmpty()){

            // Unarchive the image tarball untarDir
            tarball.untarImage()

            // Read all directories in untar
            tarball.readTempTar()

            // Get tarball array
            tarballArray = tarball.getTarballArray()

            // Get jar files in tarball
            for (TarballOperations item : tarballArray) {

                // Create JarFileOperations object
                def jarFile = new JarFileOperations(item.tarPath, item.individualFiles, item.jarDir)

                // Get jar files in each layer tarball
                jarFile.getJarName()
            }

            // Remove temporary directory
            def rmDir = new DirectoryOperations()
            rmDir.removeDir(tarballDir)

            // Query the database
            def connection = new DBInterface()
            connection.connect()
            connection.queryDBForJar(jarList)
            connection.queryDBForCVE(jarList)
            connection.closeDB()

            // Create HTML from jar array
            /*for (Jar item : vulList) {
                println "####" + item.jarName
                println "####" + item.jarDesc
                println "####" + item.cveList.size()
                for(CVE x : item.cveList){
                    println "CVE ID: " + x.cveId
                    println "CVE Description: " + x.cveDesc
                    println "CVSS Score: " + x.cveScore
                    println "CVSS Flag: " + x.cvssFlag
                    println "Access Vector: " + x.accessVector
                    println "Authentication: " + x.auth
                    println "Impact: " + x.impactType
                    println "Vulnerability Type: " + x.vulType
                    println "CWE ID: " + x.cweId
                    println "CWE Url: " + x.cweUrl
                    println "NVD URL: " + x.nvdUrl
                }
            }*/

            // Build the html report
            ReportBuilder report = new ReportBuilder()
            def html = report.build()

            // Pass html report to output file
            def reportName = "/home/Paul/Report/HTML_Vul_Simple/" + imageName + "(" + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(new Date()) + ").html"
            def index = new File(reportName)
            index << html

            // Launch security vulnerabilities report
            def url = reportName
            File htmlFile = new File(url)
            Desktop.getDesktop().browse(htmlFile.toURI())
        }
        else{

            println error.trim()
            println "The image \"" + imageName + "\" is not a valid image name."

            // Remove temporary directory
            def rmDir = new DirectoryOperations()
            rmDir.removeDir(tarballDir)

        }

    }

}

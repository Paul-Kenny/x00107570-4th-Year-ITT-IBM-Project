import java.lang.annotation.Target

/**
 * Created by Paul on 3/25/17.
 */
class ScanImage {

    String tarballDir, untarDir, jarDir, imageName
    def tarballArray = []
    static jarList = []
    static vulList = []

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

        def tarball = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        // Save the docker image tarball to the tarballDir
        tarball.dockerTar()

        // Unarchive the image tarball untarDir
        tarball.untarImage()

        // Read all directories in untar
        tarball.readTempTar()

        // Get tarball array
        tarballArray = tarball.getTarballArray()

        // Get jar files in tarball
        for (TarballOperations item : tarballArray) {
            def jarFile = new JarFileOperations(item.tarPath, item.individualFiles, item.jarDir)
            jarFile.getJarName()
        }

        // Remove temporary directory
        def rmDir = new DirectoryOperations()
        rmDir.removeDir(tarballDir)


        // Query the database
        def connection = new DBInterface()
        connection.connect()
        connection.queryDBForJar(jarList)
        connection.queryDB(jarList)
        connection.closeDB()

        println "JARLIST SIZE: " + vulList.size()

        // Create HTML from jar array
        for (Jar item : vulList) {
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
        }

    }

}

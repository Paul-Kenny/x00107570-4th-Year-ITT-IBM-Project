package com.scanImage.gradle

import java.awt.Desktop
import java.text.SimpleDateFormat

/**
 * Created by Paul on 3/27/17.
 */

class ScanImage {

    // Temporary directories and image name
    String tarballDir, untarDir, jarDir, imageName

    // Temporary directories root directory in user directory
    final TEMP_DIR = "./TemporaryDirectories/"

    // Array to store all image layer tarballs
    def tarballArray = []
    // Array to store all jars found in image
    static jarList = []
    // Array to store all jar security vulnerabilities found
    static vulList = []

    // Constructor
    ScanImage(String imageNameIn) {

        // Temporary directories object
        def mkDir = new DirectoryOperations()

        // Make temporary directories
        tarballDir = mkDir.makeDir(TEMP_DIR)
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

            // Build the html report
            ReportBuilder report = new ReportBuilder()
            def html = report.build()

            // Clear static lists
            jarList = []
            vulList = []

            // Add html markup to the output report
            def reportName = "./Report/Vulnerabilities_Reports/" + imageName + "(" + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(new Date()) + ").html"
            def index = new File(reportName)
            index << html

            // Launch security vulnerabilities report
            def url = reportName
            File htmlFile = new File(url)
            Desktop.getDesktop().browse(htmlFile.toURI())
        }
        else{

            // Display error message to console
            println error.trim()
            println "The image \"" + imageName + "\" is not a valid image name."

            // Remove temporary directories
            def rmDir = new DirectoryOperations()
            rmDir.removeDir(tarballDir)

        }

    }

}


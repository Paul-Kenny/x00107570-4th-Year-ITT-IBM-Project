package com.scanImage.gradle

import org.gradle.api.Project

import java.awt.Desktop
import java.text.SimpleDateFormat

/**
 * This class executes the security vulnerabilities scan on a chosen Docker image.
 */

class ScanImage {

    // Temporary directories and image name
    String tarballDir, untarDir, jarDir, imageName

    // Temporary directories root directory in user directory
    final TEMP_DIR = "./TemporaryDirectories/"

    // Store all image layer tarballs
    def tarballArray = []
    // Store jar objects with names with .jar extensions
    def jarObjList = []
    // Store inner jar objects with .jar extensions
    def jarWithExt = []
    // Store all jars found in image with .jar extensions removed
    def finalJarList = []
    // Store all jar security vulnerabilities found in Docker image
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
    void scanDockerImage(Project target) {

        // Setup to see if Docker image exists on system
        def inspectImage = new DockerImage()
        // Test to see if image exists
        def inspectError = inspectImage.testIfImageExists(imageName)

        // If docker image exists
        if (inspectError.isEmpty()) {

            // Create TarballOperations object
            def tarball = new TarballOperations(tarballDir, untarDir, jarDir, imageName)

            // Save the docker image tarball to the tarballDir
            tarball.dockerTar()

            // Unarchive the image tarball untarDir
            tarball.untarImage()

            // Read all directories in untar
            tarball.readTempTar()

            // Get tarball array
            tarballArray = tarball.getTarballArray()

            println "Searching image for main jar files... "
            // Get jar files in tarball
            for (TarballOperations item : tarballArray) {

                // Create JarFileOperations object
                def fileToTest = new JarFileOperations(item.tarPath, item.individualFiles, item.jarDir)

                // Test file from tarBallArray to see if it is a jar file
                def newJar = fileToTest.getJarName()

                if (newJar) {
                    // Pass returned jarFileOperations object to jarObjectList (to be used to search for inner jar files)
                    jarObjList << newJar
                    // Pass returned jarFileOperations object to jarWithExt (to store while waiting for possible inner jar files)
                    jarWithExt << newJar
                }
            }

            println "Extracting main jar files to search for inner jar files... "
            // Extract jars to test for inner jars
            for (JarFileOperations item : jarObjList) {

                // Extract jar files found to prepare for reading
                item.extractJar(item.tarPath, item.individualFiles, item.jarDir)

                // Read extracted jar files to test for inner jar files
                item.basicJarRead((item.jarDir + item.individualFiles))

                for(JarFileOperations newItem : item.getJarList()){
                    jarWithExt << newItem
                }
            }

            // Extract jars to test for inner jars
            for (JarFileOperations item : jarWithExt) {

                // Jar name with out .jar extension
                def jarWithoutExtension = item.stripJarExt(item.individualFiles)
                // Pass jar without jar file extension to finalJarList
                if(!finalJarList.contains(jarWithoutExtension)) {
                    finalJarList << jarWithoutExtension
                    println "Jar found: " + jarWithoutExtension
                }
            }

            // Remove temporary directory
            def rmDir = new DirectoryOperations()
            rmDir.removeDir(tarballDir)

            // Query the database
            def connection = new DBInterface()
            connection.connect(target)
            connection.queryDBForJar(finalJarList)
            connection.queryDBForCVE(finalJarList)
            connection.closeDB()

            // Build the html report
            ReportBuilder report = new ReportBuilder()
            def html = report.build(imageName)

            // Clear static lists
            vulList = []

            //Test if image name is complex
            if (imageName.contains("/")) {
                // Simplify image name (if necessary) for HTML report title
                imageName = imageName.substring(imageName.lastIndexOf("/"))
            }

            // Add html markup to the output report
            def reportName = "./Report/Vulnerabilities_Reports/" + imageName + "(" + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(new Date()) + ").html"
            def index = new File(reportName)
            index << html

            // Launch security vulnerabilities report
            def url = reportName
            File htmlFile = new File(url)
            Desktop.getDesktop().browse(htmlFile.toURI())

        } else {

            // Display error message to console
            println "The image \"" + imageName + "\" is not a valid image name."
            println "Please check that the image exists using the 'docker images' command"

            // Remove temporary directories
            def rmDir = new DirectoryOperations()
            rmDir.removeDir(tarballDir)

        }
    }
}


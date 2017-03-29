package com.scanImage.gradle

import java.awt.Desktop

/**
 * Created by Paul on 3/27/17.
 */

class ScanImage {

    String tarballDir, untarDir, jarDir, imageName
    def tarballArray = []
    static jarList = []

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

        //def tarball = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        // Save the docker image tarball to the tarballDir
        //tarball.dockerTar()

        // Unarchive the image tarball untarDir
        //tarball.untarImage()

        // Read all directories in untar
       // tarball.readTempTar()

        // Get tarball array
       // tarballArray = tarball.getTarballArray()

        // Get jar files in tarball
       // for (TarballOperations item : tarballArray) {
          //  def jarFile = new JarFileOperations(item.tarPath, item.individualFiles, item.jarDir)
           // jarFile.getJarName()
      //  }

        // Remove temporary directory
        def rmDir = new DirectoryOperations()
        rmDir.removeDir(tarballDir)


        // Query the database
        //def connection = new DBInterface()
        //connection.connect()
        //connection.queryDB(jarList)
        //connection.closeDB()

        def url = "/home/Paul/Desktop/x00107570-4th-Year-ITT-IBM-Project/DockerImageScan/Report/HTML_Vul/index.html"
        File htmlFile = new File(url)
        Desktop.getDesktop().browse(htmlFile.toURI())

    }

}


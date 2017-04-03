package com.scanImage.gradle

import java.awt.Desktop
import java.text.SimpleDateFormat

/**
 * Created by Paul on 3/27/17.
 */

class ScanImage {

    String tarballDir, untarDir, jarDir, imageName
    final TEMP_DIR = "./TemporaryDirectories/"
    def tarballArray = []
    static jarList = []
    static vulList = []

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
        //def connection = new DBInterface()
        //connection.connect()
        //connection.queryDBForJar(jarList)
        //connection.queryDB(jarList)
        //connection.closeDB()

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

        // Create the report markup
        ReportBuilder report = new ReportBuilder()
        def html = report.build()

        // Add html markup to the output report
        def reportName = "./Report/HTML_Vul_Simple/" + imageName + "(" + new SimpleDateFormat("dd-MM-yyyy-HH:mm").format(new Date()) + ").html"
        def index = new File(reportName)
        index << html

        // Launch security vulnerabilities report
        def url = reportName
        File htmlFile = new File(url)
        Desktop.getDesktop().browse(htmlFile.toURI())


    }

}


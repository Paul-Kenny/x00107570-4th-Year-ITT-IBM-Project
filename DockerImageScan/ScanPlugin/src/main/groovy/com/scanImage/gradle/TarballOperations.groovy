package com.scanImage.gradle

import groovy.io.FileType
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Paul on 3/25/17.
 */
class TarballOperations {

    String tarballDir, untarDir, jarDir, imageName, tarPath, individualFiles
    def tarballArray = []

    TarballOperations(String tarballDir, String untarDir, String jarDir, String imageName){
        this.tarballDir = tarballDir
        this.untarDir = untarDir
        this.jarDir = jarDir
        this.imageName = imageName
    }

    TarballOperations(String tarPath, String individualFiles, String jarDir){
        this.tarPath = tarPath
        this.individualFiles = individualFiles
        this.jarDir = jarDir
    }

    // Save the docker image as a tarball
    def dockerTar() {

        println "Creating tarball from " + imageName + " image..."

        // Create docker save command
        String dockerSave = 'docker save ' + imageName + ' --output ' + tarballDir + "/" + imageName + '.tar'
        def proc = dockerSave.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
        def error = "$err"

        // Return error report
        return error
    }

    // Unarchive docker tarball
    void untarImage() {

        println "Un-archiving " + imageName + " image..."

        //Create tarball unarchive command
        String untarCommand = "tar -xvf " + tarballDir + imageName + ".tar -C " + untarDir
        def proc = untarCommand.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
    }

    // List files in unarchived directory
    void readTempTar() {

        println "Searching un-archived image for tarball layers..."
        // Create directory content list
        def list = []

        // Read directory contents
        def dir = new File(untarDir)

        // Add directory contents to the list
        dir.eachFileRecurse(FileType.FILES) {
            file -> list << file
        }

        // Read list contents to locate tarballs
        list.each {
            findTar(it.path)
        }
    }

    // Use regex to read tarball contents
    void findTar(String path) {

        // Test for tarball regex
        String patternString = "^.*\\.(tar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(path)
        boolean matches = matcher.matches()

        // Start new tarball search
        if (matches == true) {
            basicTarRead(path)
        }
    }

    // Read and list files in tarball
    void basicTarRead(String tarPath) {

        println "Found tarball: " + tarPath
        // Read tarball File into TarArchiveInputStream
        TarArchiveInputStream myTarFile = new TarArchiveInputStream(new FileInputStream(new File(tarPath)))

        // Read individual tarball
        TarArchiveEntry entry = null
        String individualFiles

        // Read every entry in tarball
        while ((entry = myTarFile.getNextTarEntry()) != null) {

            // Get the name of the file
            individualFiles = entry.getName()

            // Get the name of all jars in tarball
            def tempTarball = new TarballOperations(tarPath, individualFiles, jarDir)
            tarballArray << tempTarball
        }
        // Close TarAchiveInputStream
        myTarFile.close()
    }

    TarballOperations [] getTarballArray(){
        return tarballArray
    }


}
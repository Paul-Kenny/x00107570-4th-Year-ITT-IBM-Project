package com.scanImage.gradle

import org.apache.commons.compress.archivers.jar.JarArchiveEntry
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Paul on 3/25/17.
 * Class to perform identification of jar files which may be contained within the Docker image layers.
 * If the are identified the name of the jar is pushed to the jarList. These jar files are also traversed
 * to identify possible jar files contained within them and if found, once again pushed to the jarList.
 */
class JarFileOperations {

    String tarPath, individualFiles, jarDir
    def jarList = []

    // Constructor
    JarFileOperations(String tarPath, String individualFiles, String jarDir) {
        this.tarPath = tarPath
        this.individualFiles = individualFiles
        this.jarDir = jarDir
    }
    // Overloaded constructor
    JarFileOperations(String individualFiles) {
        this.individualFiles = individualFiles
    }

    // Find jars and extract them to a temp directory so they can be read for inner jar files
    def getJarName() {

        try {
            // Test for jar
            def patternString = "^.*\\.(jar)\$"
            Pattern pattern = Pattern.compile(patternString)
            Matcher matcher = pattern.matcher(individualFiles)
            boolean matches = matcher.matches()


            // Search tarball for jars
            if (matches == true) {

                // Create and return jarFileOperations object
                def foundJar = new JarFileOperations(tarPath, individualFiles, jarDir)
                return foundJar
            }
        }
        catch (Exception ex) {
            println ex.printStackTrace()
        }
    }

    // Extract jar from tarball
    void  extractJar(String tarPath, String individualFiles, String destPath) {

        try {
            // Create jar extract bash command
            String extractCommand = "tar -xf " + tarPath + " -C " + destPath + " " + individualFiles

            // Execute jar extract bash command
            extractCommand.execute().waitFor()

        }
        catch (Exception ex) {
            println ex.printStackTrace()
        }
    }

    // Read and list files in jar
    def basicJarRead(String destPath) {

        try {
            // Concatenate full jar path
            String jarRead = destPath

            // Check to see if the jar path is a symbolic link
            Path SymlinkCheck = Paths.get(jarRead)

            // If it is not a symbolic link then execute basicJarRead
            if (!Files.isSymbolicLink(SymlinkCheck)) {

                // Read jar File into JarArchiveInputStream
                JarArchiveInputStream myJarFile = new JarArchiveInputStream(new FileInputStream(new File(destPath)))

                // Read individual jar file
                JarArchiveEntry entry = null
                String individualFiles

                // Read every entry in jar file
                while ((entry = myJarFile.getNextJarEntry()) != null) {

                    // Get the name of the file
                    individualFiles = entry.getName()

                    // Get the name of all jars in jar
                    getJarName(individualFiles)
                }
                // Close JarAchiveInputStream
                myJarFile.close()
            }
        }
        catch (Exception ex) {
            println ex.printStackTrace()
        }
    }

    // Overloaded getJarName method to find jar files in jar files
    def getJarName(String jarPath) {

        try {
            // Test if a jar
            String patternString = "^.*\\.(jar)\$"
            Pattern pattern = Pattern.compile(patternString)
            Matcher matcher = pattern.matcher(jarPath)
            boolean matches = matcher.matches()

            // Search jar file for inner jars
            if (matches == true) {

                // Create and pass jarFileOperations object to jarList
                JarFileOperations newJar = new JarFileOperations(jarPath)
                jarList << newJar
            }
        }
        catch (Exception ex) {
            println ex.printStackTrace()
        }
    }

    // Get inner jar list
    JarFileOperations [] getJarList(){
        return jarList
    }

    // Strip jar extension form jar name
    def stripJarExt(String jarPath) {

        try {
            // Parse the jar name without the file extension
            String withoutJarEx = jarPath.substring(jarPath.lastIndexOf("/") + 1, jarPath.lastIndexOf("."))

            // Return the jar file name without the .jar extension
            return withoutJarEx
        }
        catch (StringIndexOutOfBoundsException ex) {
            println ex.printStackTrace()
        }
    }

}

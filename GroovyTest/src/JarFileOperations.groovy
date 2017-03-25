import org.apache.commons.compress.archivers.jar.JarArchiveEntry
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Paul on 3/25/17.
 */
class JarFileOperations {

    // Find jars and extract them to a temp directory so they can be read for inner jar files
    void getJarName(String tarPath, String jarPath) {

        // Test for jar
        String patternString = "^.*\\.(jar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(jarPath)
        boolean matches = matcher.matches()

        // Search tarball for jars
        if (matches == true) {

            // Strip jar extension form jar name
            stripJarExt(jarPath)

            // Extract the jar to temproary directory
            extractJar(tarPath, jarPath, jarDir)
        }
    }

    // Extract jar from tarball
    void extractJar(String tarPath, String jarPath, String destPath) {

        // Create jar extract bash command
        String extractCommand = "tar -xf " + tarPath + " -C " + destPath + " " + jarPath

        // Execute jar extract bash command
        extractCommand.execute().waitFor()

        // Concatenate full jar path
        String jarRead = destPath + jarPath

        // Check to see if the jar path is a symbolic link
        Path SymlinkCheck = Paths.get(jarRead)

        // If it is not a symbolic link then execute basicJarRead
        if (!Files.isSymbolicLink(SymlinkCheck)) {

            // Read the jar file for inner jar files
            basicJarRead(jarRead)
        }
    }

    // Read and list files in jar
    void basicJarRead(String jarPath) {

        // Read jar File into JarArchiveInputStream
        JarArchiveInputStream myJarFile = new JarArchiveInputStream(new FileInputStream(new File(jarPath)))

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

    // Overloaded getJarName method to find jar files in jar files
    void getJarName(String jarPath) {

        // Test if a jar
        String patternString = "^.*\\.(jar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(jarPath)
        boolean matches = matcher.matches()

        // Search jar file for inner jars
        if (matches == true) {

            // Strip jar extension form jar name
            stripJarExt(jarPath)
        }
    }

    // Strip jar extension form jar name
    void stripJarExt(String jarPath) {

        // Parse the jar name without the file extension
        String withoutJarEx = jarPath.substring(jarPath.lastIndexOf("/") + 1, jarPath.indexOf("."))

        // Test if the jar is a third party jar
        thirdPartyJarTest(withoutJarEx)
    }

    // Test if jar is third party jar
    void thirdPartyJarTest(String jar) {

        // Regex to identify if jar name matches third party signature
        String patternString = "^([a-z]*-[a-z0-9\\\\.]*){2}\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(jar)
        boolean matches = matcher.matches()

        // If jar third party pass it to the jar array
        if (matches == true) {

            // Add jar name to jar array
            addToJarArray(jar)
        }
    }

    // Add jar name to jar array
    void addToJarArray(String jarPath) {

        // Add jar name to jar array
        if (!jarList.contains(jarPath)) {
            jarList << jarPath
        }
    }
}

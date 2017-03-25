import org.apache.commons.compress.archivers.jar.JarArchiveEntry
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern
import java.util.regex.Matcher
import groovy.io.FileType

class DockerImageScan {

    String tarballDir, untarDir, jarDir, imageName
    def jarList = []

    DockerImageScan(String imageNameIn, String tempDir) {

        // Temporary directories object
        def mkdir = new DirectoryOperations()

        // Make temporary directories
        tarballDir = mkdir.makeDir(tempDir)
        untarDir = mkdir.makeDir(tarballDir)
        jarDir = mkdir.makeDir(untarDir)
        imageName = imageNameIn
    }

    // Start the scan process
    void scanImage() {

        // Save the docker image tarball to the tarballDir
        dockerTar(imageName, tarballDir)

        // Unarchive the image tarball untarDir
        untarImage(imageName, tarballDir, untarDir)

        // Read all directories in untar
        readTempTar(untarDir)

        // Create delete temporary directory command
        String removeTemp = "rm -rf " + tarballDir

        // Execute delete temporary directory command
        removeTemp.execute()

        // Query the database
        queryDB()
    }

    // Save the docker image as a tarball
    void dockerTar(String imageName, String tempDirPath) {

        // Create docker save command
        String dockerSave = 'docker save ' + imageName + ' --output ' + tempDirPath + "/" + imageName + '.tar'
        def proc = dockerSave.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
    }

    // Unarchive docker tarball
    void untarImage(String imageName, String tarballDir, String untarDir) {

        //Create tarball unarchive command
        String untarCommand = "tar -xvf " + tarballDir + imageName + ".tar -C " + untarDir
        def proc = untarCommand.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
    }

    // List files in unarchived directory
    void readTempTar(String tempDir) {

        // Create directory content list
        def list = []

        // Read directory contents
        def dir = new File(tempDir)

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
            getJarName(tarPath, individualFiles)
        }
        // Close TarAchiveInputStream
        myTarFile.close()
    }

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

            // Extract the jar to temporary directory
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

    // Query the vulnerabilities database for jar vulnerabilities
    void queryDB() {

        // Create DBInterface object
        def connection = new DBInterface()

        // Connect to database
        connection.connect()

        // Query the database
        connection.queryDB(jarList)

        // Close DB connection
        connection.closeDB()
    }

}


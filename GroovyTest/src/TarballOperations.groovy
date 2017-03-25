import groovy.io.FileType
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Paul on 3/25/17.
 */
class TarballOperations {

    String jarDir

    TarballOperations(String jarDir){
        this.jarDir = jarDir
    }

    // Save the docker image as a tarball
    void dockerTar(String imageName, String tempDirPath) {

        println "Creating tarball from " + imageName + " image..."
        // Create docker save command
        String dockerSave = 'docker save ' + imageName + ' --output ' + tempDirPath + "/" + imageName + '.tar'
        def proc = dockerSave.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
    }

    // Unarchive docker tarball
    void untarImage(String imageName, String tarballDir, String untarDir) {

        println "Un-archiving " + imageName + " image..."
        //Create tarball unarchive command
        String untarCommand = "tar -xvf " + tarballDir + imageName + ".tar -C " + untarDir
        def proc = untarCommand.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
    }

    // List files in unarchived directory
    void readTempTar(String tempDir) {

        println "Searching un-archived image for tarball layers..."
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

        println "Found tarball: " + tarPath
        // Read tarball File into TarArchiveInputStream
        TarArchiveInputStream myTarFile = new TarArchiveInputStream(new FileInputStream(new File(tarPath)))

        // Read individual tarball
        TarArchiveEntry entry = null
        String individualFiles

        // Read every entry in tarball
        while ((entry = myTarFile.getNextTarEntry()) != null) {

            def jarFile = new JarFileOperations()
            // Get the name of the file
            individualFiles = entry.getName()

            // Get the name of all jars in tarball
            jarFile.getJarName(tarPath, individualFiles, jarDir)
        }
        // Close TarAchiveInputStream
        myTarFile.close()
    }


}

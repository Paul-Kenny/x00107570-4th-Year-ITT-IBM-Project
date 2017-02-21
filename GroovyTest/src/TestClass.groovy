import org.apache.commons.compress.archivers.jar.JarArchiveEntry
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.io.*
import java.util.regex.Pattern
import java.util.regex.Matcher
import groovy.io.FileType

/**
 * Created by Paul on 25-Jan-17.
 */

class TestClass {

    // Start the scan process
    void start(String imageName, String tempDir){

        // Make temp directory to store the docker image tarball
        String tarballDir= makeDir(tempDir)
        // Make inner directory to store the unarchived tarball
        String untarDir = makeDir(tarballDir)
        // Make temp jar directory
        String jarDir = makeDir(untarDir)

        // Save the docker image tarball to the tarballDir
        dockerTar(imageName, tarballDir)

        // Unarchive the image tarball untarDir
        untarImage(imageName, tarballDir, untarDir)

        //Read all directories in untar
        readTempTar(untarDir)

        //Create delete temporary directory command
        //String removeTemp = "rm -rf " + untarDir
        //Execute delete temporary directory command
        //removeTemp.execute()

        //Create delete temporary directory command
        //removeTemp = "rm -rf " + tarballDir
        //Execute delete temporary directory command
        //removeTemp.execute()
    }

    // Make the temporary directory
    String makeDir(String tempDir){
        //Create temporary tar directory path
        tempDir =  tempDir + "Temp/"
        //Create make directory string
        String makeDir = "mkdir " + tempDir
        //Execute make directory
        makeDir.execute().waitFor()
        return tempDir
    }

    // Save the docker image as a tarball
    void dockerTar(String imageName, String tempDirPath){
        String dockerSave = 'docker save ' + imageName + ' --output ' + tempDirPath + "/" + imageName + '.tar'
        def proc = dockerSave.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out,err)
        proc.waitFor()
    }

    //Untar docker tar
    void untarImage(String imageName, String tarballDir, String untarDir){
        //Create untar string
        String untarCommand = "tar -xvf " + tarballDir + imageName + ".tar -C " + untarDir
        def proc = untarCommand.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out,err)
        proc.waitFor()
    }

    //List files in untarred directory
    void readTempTar(String tempDir){
        //Create directory content list array
        def list = []

        //Read directory contents
        def dir = new File(tempDir)
        //Add directory contents to the list array
        dir.eachFileRecurse (FileType.FILES) {
            file -> list << file
        }
        //Read list array contents to locate tars
        list.each {
            findTar(it.path)
        }
    }

    //Use regex to read tar contents
    void findTar(String path){
        //Test for tar regex
        String patternString =  "^.*\\.(tar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(path)
        boolean matches = matcher.matches()

        //If statement to start new tar search
        if(matches == true) {
            println(path)
            basicTarRead(path)
        }
    }

    //Read and list files in tar
    void basicTarRead(String tarPath){
        // Read TAR File into TarArchiveInputStream
        TarArchiveInputStream myTarFile=new TarArchiveInputStream(new FileInputStream(new File(tarPath)))
        // Read individual TAR file
        TarArchiveEntry entry = null
        String individualFiles
        // While loop to read every single entry in TAR file
        while ((entry = myTarFile.getNextTarEntry()) != null) {
            // Get the name of the file
            individualFiles = entry.getName()
            //Get the name of all jars in tar
            getJarName(tarPath, individualFiles)
        }
        /* Close TarAchiveInputStream */
        myTarFile.close()
    }

    ////Use regex to find jars
    void getJarName(String tarPath, String filePath){
        //Test for jar regex
        String patternString =  "^.*\\.(jar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(filePath)
        boolean matches = matcher.matches()

        //Search tars for jars
        if(matches == true){
            println("Jar name: " + filePath )
            String myString = filePath
            //Parse the jar name without the file extension
            String withoutJarEx = myString.substring(myString.lastIndexOf("/")+1, myString.indexOf("."))
            //Parse the jar name with the file extension
            String withJarEx = myString.substring(myString.lastIndexOf("/")+1)
            println("New jar without extension: " + withoutJarEx)
            println("New jar with extension: " + withJarEx)
            extractJar(tarPath, filePath)
        }

    }

    //Extract jar from tar
    void extractJar(String tarPath, String filePath){
        String extractCommand = "tar -xf " + tarPath + " -C /home/Paul/Temp/Temp/Temp " + filePath
        extractCommand.execute().waitFor()

        String jarRead = "/home/Paul/Temp/Temp/Temp/" + filePath
        if(jarRead != "/home/Paul/Temp/Temp/Temp/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/java-atk-wrapper.jar") {
            println "#########Inner Jar Content###############"
            basicJarRead(jarRead)
            println "#########################################"
        }
    }

    //Read and list files in tar
    void basicJarRead(String jarPath){
        // Read TAR File into TarArchiveInputStream
        JarArchiveInputStream myJarFile=new JarArchiveInputStream(new FileInputStream(new File(jarPath)))
        // Read individual TAR file
        JarArchiveEntry entry = null
        String individualFiles
        // While loop to read every single entry in TAR file
        while ((entry = myJarFile.getNextJarEntry()) != null) {
            // Get the name of the file
            individualFiles = entry.getName()
            getInnerJarName(individualFiles)
        }
        /* Close JarAchiveInputStream */
        myJarFile.close()
    }

    ////Use regex to find jars
    void getInnerJarName(String filePath){
        //Test for jar regex
        String patternString =  "^.*\\.(jar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(filePath)
        boolean matches = matcher.matches()

        //Search tars for jars
        if(matches == true){
            String myString = filePath
            //Parse the jar name without the file extension
            String withoutJarEx = myString.substring(myString.lastIndexOf("/")+1, myString.indexOf("."))
            //Parse the jar name with the file extension
            String withJarEx = myString.substring(myString.lastIndexOf("/")+1)
            println("New inner jar without extension: " + withoutJarEx)
            println("New inner jar with extension: " + withJarEx)
        }

    }

}


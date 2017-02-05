import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.regex.Pattern
import java.util.regex.Matcher
import groovy.io.FileType

/**
 * Created by Paul on 25-Jan-17.
 */

class TestClass {

    /*//Test to see if docker command can be run on shell
    def executeOnShell(String command1, String command2) {
        String command = (command1 + command2)
        return executeOnShell(command, new File('/home/Paul'))
    }

    def executeOnShell(String command, File workingDir) {
        println command
        def process = new ProcessBuilder(command)
                .directory(workingDir)
                .redirectErrorStream(true)
                .start()
        process.inputStream.eachLine {println it}
        process.waitFor()
        return process.exitValue()
    }*/

    //Untar docker tar
    void untarImage(String imageName, String dir){
        //Create temporary tar directory path
        String tempDir =  dir + "TempTar"
        //Create make directory string
        String makeDir = "mkdir " + tempDir
        //Execute make directory
        makeDir.execute()
        //Create untar string
        String untarCommand = "tar -xvf " + dir + imageName + " -C " + tempDir
        //Execute untar command
        untarCommand.execute()
        //Read all directories in untar
        readTempTar(tempDir)
        //Create delete temporary directory command
        String removeTemp = "rm -rf " + tempDir
        //Execute delete temporary directory command
        //removeTemp.execute()
    }

    //List files in untarred directory
    void readTempTar(String tempDir){
        //Create directory content list array
        def list = []

        //Read directory contents
        def dir = new File(tempDir)
        //Add directory contents to the list array
        dir.eachFileRecurse (FileType.FILES) { file ->
            list << file
        }
        println "###################################"
        //Read list array contents to locate tars
        list.each {
            findTar(it.path)
        }
    }

    //Use regex to read tar contents
    void findTar(String path){
        String patternString =  "^.*\\.(tar)\$";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(path);
        boolean matches = matcher.matches();

        //If statement to start new tar search
        if(matches == true) {
            System.out.println("This" + path)
            basicTarRead(path)
        }
    }

    //Read and list files in tar
    void basicTarRead(String tarPath){
        /* Read TAR File into TarArchiveInputStream */
        TarArchiveInputStream myTarFile=new TarArchiveInputStream(new FileInputStream(new File(tarPath)));
        /* Read individual TAR file */
        TarArchiveEntry entry = null
        String individualFiles
        int offset
        FileOutputStream outputFile=null
        /* While loop to read every single entry in TAR file */
        while ((entry = myTarFile.getNextTarEntry()) != null) {
            /* Get the name of the file */
            individualFiles = entry.getName()
            /* SOP statement to check progress */
            //System.out.println("xxFile Name in TAR File is: " + individualFiles)
            getJarName(individualFiles)
        }
        /* Close TarAchiveInputStream */
        myTarFile.close()
    }

    //Parse jar names
    void getJarName(String filePath){
        /*Test for jar regex*/
        String patternString =  "^.*\\.(jar)\$"
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(filePath)
        boolean matches = matcher.matches()

        //If statement to start new tar search
        if(matches == true){
            System.out.println("Jar name: " + filePath )
        }
    }
}


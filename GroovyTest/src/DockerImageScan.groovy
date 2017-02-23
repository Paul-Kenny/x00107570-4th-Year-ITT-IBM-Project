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
        def jarListFile

        DockerImageScan(String imageNameIn, String tempDir){
            tarballDir = makeDir(tempDir)
            untarDir = makeDir(tarballDir)
            jarDir = makeDir(untarDir)
            imageName = imageNameIn
            jarListFile = new File('/home/Paul/jarTest.txt')
        }

        // Start the scan process
        void scanImage(){
            // Save the docker image tarball to the tarballDir
            dockerTar(imageName, tarballDir)

            // Unarchive the image tarball untarDir
            untarImage(imageName, tarballDir, untarDir)

            //Read all directories in untar
            readTempTar(untarDir)

            //Create delete temporary directory command
            String removeTemp = "rm -rf " + tarballDir
            //Execute delete temporary directory command
            removeTemp.execute()
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

        //Use regex to find jars and extract them to a temp directory
        void getJarName(String tarPath, String jarPath){
            //Test for jar regex
            String patternString =  "^.*\\.(jar)\$"
            Pattern pattern = Pattern.compile(patternString)
            Matcher matcher = pattern.matcher(jarPath)
            boolean matches = matcher.matches()

            //Search tars for jars
            if(matches == true){
                printJarName(jarPath)
                extractJar(tarPath, jarPath, jarDir)
            }
        }

        //Extract jar from tar
        void extractJar(String tarPath, String jarPath, String destPath){
            // Create jar extract bash command
            String extractCommand = "tar -xf " + tarPath + " -C " + destPath + " " + jarPath
            // Execute jar extract bash command
            extractCommand.execute().waitFor()
            // Concatenate full jar path
            String jarRead = destPath + jarPath
            // Check to see if the jar path is a symbolic link
            Path SymlinkCheck = Paths.get(jarRead)
            // If it is not a symbolic link then execute basicJarRead
            if(!Files.isSymbolicLink(SymlinkCheck)) {
                basicJarRead(jarRead)
            }
        }

        //Read and list files in jar
        void basicJarRead(String jarPath){
            // Read JAR File into JarArchiveInputStream
            JarArchiveInputStream myJarFile=new JarArchiveInputStream(new FileInputStream(new File(jarPath)))
            // Read individual JAR file
            JarArchiveEntry entry = null
            String individualFiles
            // While loop to read every single entry in JAR file
            while ((entry = myJarFile.getNextJarEntry()) != null) {
                // Get the name of the file
                individualFiles = entry.getName()
                getJarName(individualFiles)
            }
            /* Close JarAchiveInputStream */
            myJarFile.close()
        }

        //// Overloaded getJarName method. Use regex to find jars
        void getJarName(String jarPath){
            //Test for jar regex
            String patternString =  "^.*\\.(jar)\$"
            Pattern pattern = Pattern.compile(patternString)
            Matcher matcher = pattern.matcher(jarPath)
            boolean matches = matcher.matches()

            //Search tars for jars
            if(matches == true){
                // Print the jar name
                printJarName(jarPath)
            }
        }

        // Print the jar names
        void printJarName(String jarPath){
                //Parse the jar name without the file extension
                String withoutJarEx = jarPath.substring(jarPath.lastIndexOf("/")+1, jarPath.indexOf("."))
                //Parse the jar name with the file extension
                String withJarEx = jarPath.substring(jarPath.lastIndexOf("/")+1)
                // Write jar names to external file
                jarListFile << withJarEx + "\n"
        }

    }


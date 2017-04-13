import com.scanImage.gradle.DirectoryOperations
import com.scanImage.gradle.JarFileOperations
import com.scanImage.gradle.TarballOperations
import spock.lang.Specification

/**
 * Created by Paul on 4/12/17.
 */
class JarFileOpsTests extends Specification{

    def "Call getJarName() method on jar file"(){

        setup:
        // Declare and initialise directory and image names
        String tarPath = "/TestPath/"
        String jarDir = "/TestPath/"
        String individualFiles = "testFile.jar"

        when:
        // Create JarFileOperations object
        def testIfJarFile = new JarFileOperations(tarPath, individualFiles, jarDir)
        // Create test state
        def result =testIfJarFile.getJarName()

        then:
        // Test to see if jar parsed correctly
        result.tarPath == "/TestPath/"
        result.individualFiles == "testFile.jar"
        result.jarDir == "/TestPath/"
    }

    def "Call getJarName() method on non jar file"(){

        setup:
        // Declare and initialise directory and image names
        String tarPath = "/TestPath/"
        String jarDir = "/TestPath/"
        String individualFiles = "testFile.class"

        when:
        // Create JarFileOperations object
        def testIfJarFile = new JarFileOperations(tarPath, individualFiles, jarDir)
        // Create test state
        def result =testIfJarFile.getJarName()

        then:
        // Test to see if null object returned if file is not a jar file
        result == null
    }

    def "Call extractJar() method on jar file"(){

        setup:
        // Create DirectoryOperations object
        DirectoryOperations dirOps = new DirectoryOperations()
        def temp = dirOps.makeDir(' -p ./src/TestDirectory/JarFileOpsTestDir/Temp/Temp/')

        // Declare and initialise directory and image names
        String tarballDir = "./src/TestDirectory/JarFileOpsTestDir/Temp/"
        String untarDir = "./src/TestDirectory/JarFileOpsTestDir/Temp/Temp/"
        String jarDir = "./src/TestDirectory/JarFileOpsTestDir/Temp/Temp/Temp/"
        String imageName = "artifactory.swg.usma.ibm.com:6555/toscana-service-chat"

        // Create temporary directory and file name to test new tarball existence
        def fileName = "./src/TestDirectory/JarFileOpsTestDir/Temp/Temp/Temp/opt/ibm/toscana/chat/toscana-service-chat-20170404-132234-standalone.jar"
        def testFile = new File(fileName)

        // Create temp list
        def tarballArray = []
        // Jar test array
        def jarArray = []
        // Create TarballOperations object
        def tarball = new TarballOperations(tarballDir, untarDir, jarDir, imageName)

        // Save the docker image tarball to the tarballDir
        tarball.dockerTar()

        // Unarchive the image tarball untarDir
        tarball.untarImage()

        // Read all directories in untar
        tarball.readTempTar()

        // Get tarball array
        tarballArray = tarball.getTarballArray()

        println "Searching image for main jar files... "
        // Get jar files in tarball
        for (TarballOperations item : tarballArray) {

            // Create JarFileOperations object
            def fileToTest = new JarFileOperations(item.tarPath, item.individualFiles, item.jarDir)

            // Test file from tarBallArray to see if it is a jar file
            def newJar = fileToTest.getJarName()

            if (newJar) {
                // Pass returned jarFileOperations object to jarObjectList (to be used to search for inner jar files)
                jarArray << newJar
            }
        }

        when:

        // Extract jars to test for inner jars
        for (JarFileOperations item : jarArray) {
            item.extractJar(item.tarPath, item.individualFiles, item.jarDir)
        }

        // Declare test state
        def result = true
        // Change test state if jar file exist (it has been successfully extracted)
        if(testFile.exists()){
            result = true
        }

        then:
        result == true

    }

    def "Call basicJarRead() method on jar file"(){

        setup:
        // Declare and initialise directory and jar file name
        String tarPath = "/TestPath/"
        String jarDir = "./src/TestDirectory/JarFileOpsTestDir/Temp/Temp/Temp/opt/ibm/toscana/chat/"
        String individualFile = "toscana-service-chat-20170404-132234-standalone.jar"

        // Name of expected inner test jar
        String expectedTestJar =  "lib/commons-lang3-3.4.jar"

        // Jar test array
        def jarArray = []

        when:

        def result
        JarFileOperations testJar = new JarFileOperations(tarPath, individualFile, jarDir)
        // Read extracted jar files to test for inner jar files
        testJar.basicJarRead((testJar.jarDir + testJar.individualFiles))

        for(JarFileOperations newItem : testJar.getJarList()){
            jarArray << newItem
        }

        JarFileOperations testFile = jarArray.first()

        if(testFile.individualFiles == expectedTestJar){
            result = true
        }

        then:
        result == true

        /*cleanup:
        // Remove test directories
        String removeDir = "rm -rf ./src/TestDirectory"
        removeDir.execute().waitFor()*/

    }

    def "Call stripJarExt() method on"(){

        setup:
        // Declare jar file name with path and .jar extension to be stripped
        String testJarName = "lib/commons-lang3-3.4.jar"

        // Expected jar name without with path and .jar extension
        String expectedTestJarName =  "commons-lang3-3.4"

        when:
        def result

        JarFileOperations testJar = new JarFileOperations(testJarName)

        def strippedJarName = testJar.stripJarExt(testJarName)

        if(strippedJarName == expectedTestJarName){
            result = true
        }

        then:
        result == true

        cleanup:
        // Remove test directories
        String removeDir = "rm -rf ./src/TestDirectory"
        removeDir.execute().waitFor()

    }
}

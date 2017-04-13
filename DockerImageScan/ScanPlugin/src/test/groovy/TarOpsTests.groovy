import com.scanImage.gradle.DirectoryOperations
import com.scanImage.gradle.TarballOperations
import spock.lang.Specification


/**
 * Created by Paul on 4/11/17.
 */
class TarOpsTests extends Specification{

    def "Make a tarball from a Docker image"() {
        setup:
        // Create DirectoryOperations object
        DirectoryOperations dirOps = new DirectoryOperations()
        def temp = dirOps.makeDir(' -p ./src/TestDirectory/TarOpsTestDir/Temp/Temp/')

        // Declare and initialise directory and image names
        String tarballDir = "./src/TestDirectory/TarOpsTestDir/Temp/"
        String untarDir = "./src/TestDirectory/TarOpsTestDir/Temp/Temp/"
        String jarDir = "./src/TestDirectory/TarOpsTestDir//Temp/Temp/Temp/"
        String imageName = "hello-world"


        // Create tarballOperations object
        TarballOperations tarOps = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        // Call dockerTar method to create a tarball from a chosen image
        tarOps.dockerTar()

        // Create temporary directory and file name to test new tarball existence
        def fileName = "./src/TestDirectory/TarOpsTestDir/Temp/temp.tar"
        def testFile = new File(fileName)

        when:
        // Declare test state
        def result
        // Change test state if tarball exist (it has been successfully created)
        if(testFile.exists()){
            result = true
        }

        then:
        // Test to see if tarball has been created
        result == true


    }

    def "Un-archive Docker image tarball"() {

        setup:
        // Declare and initialise directory and image names
        String tarballDir = "./src/TestDirectory/TarOpsTestDir/Temp/"
        String untarDir = "./src/TestDirectory/TarOpsTestDir/Temp/Temp/"
        String jarDir = "./src/TestDirectory/TarOpsTestDir//Temp/Temp/Temp/"
        String imageName = "hello-world"

        // Create tarballOperations object
        TarballOperations tarOps = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        // Call dockerTar method to create a tarball from a chosen image
        tarOps.untarImage()

        // Create temporary directory and file name to test image layer tarball existence
        def fileName = "./src/TestDirectory/TarOpsTestDir/Temp/Temp/c54a2cc56cbb2f04003c1cd4507e118af7c0d340fe7e2720f70976c4b75237dc.json"
        def testFile = new File(fileName)

        when:
        // Declare test state
        def result

        // Change test state if image layer tarball exists and thus Docker image has been un-archived successfully
        if(testFile.exists()){
            result = true
        }

        then:
        // Test to see if Docker image has been un-archived successfully
        result == true
    }

    def "Read Docker image layer tarballs from Docker image tarball"() {

        setup:
        // Declare and initialise directory and image names
        String tarballDir = "./src/TestDirectory/TarOpsTestDir/Temp/"
        String untarDir = "./src/TestDirectory/TarOpsTestDir/Temp/Temp/"
        String jarDir = "./src/TestDirectory/TarOpsTestDir//Temp/Temp/Temp/"
        String imageName = "hello-world"

        // Declare and initialise tarPath and individualFiles names expected to be found in test results
        String tarPath = "./src/TestDirectory/TarOpsTestDir/Temp/Temp/93b4842617200e98078b410b57643804f8fdb6d6c2622182b1920e3c88ac0b07/layer.tar"
        String individualFiles = "hello"

        // Create tarballOperations object
        TarballOperations tarOps1 = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        // Call readTempTar method to read all un-archived Docker image layer tarballs
        tarOps1.readTempTar()

        when:
        // Declare test state
        def result
        // Create new tarballOperations object
        TarballOperations tarOps2

        // Pass tarballOperations object from tarBallArray to new tarOps object
        for (TarballOperations item : tarOps1.tarballArray) {
            tarOps2 = new TarballOperations(item.tarPath, item.individualFiles, item.jarDir)
        }

        // Test to see if new object contains expected elements
        if((tarOps2.tarPath == tarPath) && (tarOps2.individualFiles == individualFiles) && (tarOps2.jarDir == jarDir)){
            result = true
        }

        then:
        // Test if Docker image tarball read successfully
        result == true
    }

    def "Test TarballOperations getTarballArray()"() {

        setup:
        // Declare and initialise directory and image names
        String tarballDir = "/home/Paul/TestDir/Temp/"
        String untarDir = "/home/Paul/TestDir/Temp/Temp/"
        String jarDir = "/home/Paul/TestDir/Temp/Temp/Temp/"
        String imageName = "hello-world"

        // Declare and initialise tarPath and individualFiles names expected to be found in test results
        String tarPath = "/home/Paul/TestDir/Temp/Temp/93b4842617200e98078b410b57643804f8fdb6d6c2622182b1920e3c88ac0b07/layer.tar"
        String individualFiles = "hello"

        // Create tarballOperations object
        TarballOperations tarOps = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        // Call readTempTar method to read all un-archived Docker image layer tarballs
        tarOps.readTempTar()
        // Declare newTarBallArray and call getTarballArray on tarOps object to populate it
        def newTarBallArray = tarOps.getTarballArray()

        when:
        // Declare test state
        def result
        // Create new tarballOperations object
        TarballOperations tarOps2

        // Pass tarballOperations object from newTarBallArray to new tarOps object
        for (TarballOperations item : newTarBallArray) {
            tarOps2 = new TarballOperations(item.tarPath, item.individualFiles, item.jarDir)
        }

        // Test to see if new object contains expected elements
        if((tarOps2.tarPath == tarPath) && (tarOps2.individualFiles == individualFiles) && (tarOps2.jarDir == jarDir)){
            result = true
        }

        then:
        // Test if Docker image tarball read successfully
        result == true

        cleanup:
        // Remove test directories
        String removeDir = "rm -rf ./src/TestDirectory"
        removeDir.execute().waitFor()
    }

}

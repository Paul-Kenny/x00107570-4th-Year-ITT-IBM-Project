import com.scanImage.gradle.TarballOperations
import spock.lang.Specification


/**
 * Created by Paul on 4/11/17.
 */
class TarOpsTest extends Specification{

    def "Make a tarball from a Docker image"() {

        setup:
        String tarballDir = "/home/Paul/TestDir/Temp/"
        String untarDir = "/home/Paul/TestDir/Temp/Temp/"
        String jarDir = "/home/Paul/TestDir/Temp/Temp/Temp/"
        String imageName = "hello-world"

        TarballOperations tarOps = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        tarOps.dockerTar()

        def fileName = "/home/Paul/TestDir/Temp/temp.tar"
        def testFile = new File(fileName)

        when:
        def result

        if(testFile.exists()){
            result = true
        }

        then:
        result == true
    }

    def "Un-archive Docker image tarball"() {

        setup:
        String tarballDir = "/home/Paul/TestDir/Temp/"
        String untarDir = "/home/Paul/TestDir/Temp/Temp/"
        String jarDir = "/home/Paul/TestDir/Temp/Temp/Temp/"
        String imageName = "hello-world"

        TarballOperations tarOps = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        tarOps.untarImage()

        def fileName = "/home/Paul/TestDir/Temp/Temp/c54a2cc56cbb2f04003c1cd4507e118af7c0d340fe7e2720f70976c4b75237dc.json"
        def testFile = new File(fileName)

        when:
        def result

        if(testFile.exists()){
            result = true
        }

        then:
        result == true
    }

    def "Read all Docker image layer tarballs from Docker image tarball"() {

        setup:
        String tarballDir = "/home/Paul/TestDir/Temp/"
        String untarDir = "/home/Paul/TestDir/Temp/Temp/"
        String jarDir = "/home/Paul/TestDir/Temp/Temp/Temp/"
        String imageName = "hello-world"
        String tarPath = "/home/Paul/TestDir/Temp/Temp/93b4842617200e98078b410b57643804f8fdb6d6c2622182b1920e3c88ac0b07/layer.tar"
        String individualFiles = "hello"

        TarballOperations tarOps1 = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        tarOps1.readTempTar()

        when:
        def result
        TarballOperations tarOps2

        // Get jar files in tarball
        for (TarballOperations item : tarOps1.tarballArray) {
            tarOps2 = new TarballOperations(item.tarPath, item.individualFiles, item.jarDir)
        }

        println tarOps2.tarPath
        println tarOps2.jarDir
        println tarOps2.individualFiles

        if((tarOps2.tarPath == tarPath) && (tarOps2.individualFiles == individualFiles) && (tarOps2.jarDir == jarDir)){
            result = true
        }

        then:
        result == true
    }

    /*def "Test TarballOperations getTarballArray()"() {

        setup:
        String tarballDir = "/home/Paul/TestDir/Temp/"
        String untarDir = "/home/Paul/TestDir/Temp/Temp/"
        String jarDir = "/home/Paul/TestDir/Temp/Temp/Temp/"
        String imageName = "hello-world"
        String tarPath = "/home/Paul/TestDir/Temp/Temp/93b4842617200e98078b410b57643804f8fdb6d6c2622182b1920e3c88ac0b07/layer.tar"
        String individualFiles = "hello"
        def newTarBallArray = []

        TarballOperations tarOps = new TarballOperations(tarballDir, untarDir, jarDir, imageName)
        TarballOperations tarOps2 = new TarballOperations(tarPath, individualFiles, jarDir)
        tarOps.tarballArray << tarOps2
        newTarBallArray = tarOps.getTarballArray()

        when:
        def result

        TarballOperations tarOps3

        // Get jar files in tarball
        for (TarballOperations item : newTarBallArray) {
            tarOps3 = new TarballOperations(item.tarPath, item.individualFiles, item.jarDir)
        }

        println tarOps2.tarPath
        println tarOps2.jarDir
        println tarOps2.individualFiles

        if((tarOps2.tarPath == tarOps3.tarPath) && (tarOps2.individualFiles == tarOps3.individualFiles) && (tarOps2.jarDir == tarOps3.jarDir)){
            result = true
        }

        then:
        result == true
    }*/



}

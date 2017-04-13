/**
 * Created by Paul on 4/11/17.
 */


import com.scanImage.gradle.DirectoryOperations
import spock.lang.Specification

class DirOpsTests extends Specification{

    def "Make temporary directory"() {

        setup:
        // Create DirectoryOperations object
        DirectoryOperations dirOps = new DirectoryOperations()

        when:
        //Call the makeDir method
        def result = dirOps.makeDir(' -p ./src/TestDirectory/DirOpsTestDir/')

        then:
        // Test to see if new directory has been made
        result == ' -p ./src/TestDirectory/DirOpsTestDir/Temp/'
    }

    def "Remove temporary directory"() {

        setup:
        // Create DirectoryOperations object
        DirectoryOperations dirOps = new DirectoryOperations()

        when:
        // Create temporary directory name to test existence
        def newDir = new File('./src/TestDirectory/DirOpsTestDir/Temp')

        // Call removeDir method
        dirOps.removeDir('./src/TestDirectory/DirOpsTestDir/Temp')

        // Declare test state
        def result

        // Change test state if directory does not exist (it has been removed)
        if(!newDir.exists()){
            result = true
        }

        then:
        // Test to see if new directory has been removed
        result == true

        cleanup:
        // Remove test directories
        String removeDir = "rm -rf ./src/TestDirectory"
        removeDir.execute().waitFor()
    }
}
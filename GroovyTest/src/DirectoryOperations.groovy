/**
 * Created by Paul on 3/21/17.
 */
class DirectoryOperations {

    // Make the temporary directory
    String makeDir(String tempDir) {

        println "Creating temporary directory " + tempDir

        // Create temporary tar directory path
        tempDir = tempDir + "Temp/"

        // Create make directory string
        String makeDir = "mkdir " + tempDir

        // Execute make directory
        makeDir.execute().waitFor()

        return tempDir
    }

    void removeDir(String tempDir){
        // Create delete temporary directory command
        String removeTemp = "rm -rf " + tempDir

        // Execute delete temporary directory command
        removeTemp.execute().waitFor()

        println tempDir + " removed"
    }
}

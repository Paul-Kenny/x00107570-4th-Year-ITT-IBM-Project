package com.scanImage.gradle

/**
 * Created by Paul on 4/11/17.
 */
class DockerImage {

    // Save the docker image as a tarball
    def testIfImageExists(String imageName) {

        try {
            println "Testing existence of " + imageName + " image..."

            // Create docker save command
            String dockerInspect = 'docker inspect ' + imageName
            def proc = dockerInspect.execute()
            def out = new StringBuilder(), err = new StringBuilder()
            proc.consumeProcessOutput(out, err)
            proc.waitFor()
            def error = "$err"

            // Return error report
            return error

        } catch (Exception ex) {
            println ex.printStackTrace()
        }
    }
}

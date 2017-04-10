package com.scanImage.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * This is the custom Gradle task that implements the initiation of the Docker image security vulnerability scan.
 * It takes the Docker image name as a command line argument and scans the image for security vulnerabilities
 * associated with jar files contained in the image.
 */

class runScanImage implements Plugin<Project> {
    void apply(Project target) {
        target.task('scan') {
            doLast {

                // Read image name/id from CLI argument
                def imageNameArgument = target.hasProperty('imageNameArg') ? target.property('imageNameArg') : 'defaultName'

                // Run the vulnerabilities scan
                def imageScan = new ScanImage(imageNameArgument)
                imageScan.scanDockerImage(target)

            }
        }
    }

}
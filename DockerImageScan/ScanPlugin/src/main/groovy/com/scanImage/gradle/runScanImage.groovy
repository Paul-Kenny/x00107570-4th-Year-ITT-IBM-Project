package com.scanImage.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin

class runScanImage implements Plugin<Project> {
    void apply(Project target) {
        target.task('scan') {

            doLast {
                // Resolve JDBC driver
                // TODO This code should be moved into the layer that handles all the DB logic.
                // I just wrote it here for convenience as it needs access to the Project instance.
                def jdbcDriverConfig = target.getConfigurations().create('driver')
                target.getDependencies().add(jdbcDriverConfig.name, 'mysql:mysql-connector-java:6.0.6')
                URLClassLoader loader = GroovyObject.class.classLoader
                target.getConfigurations().driver.each { File file ->
                    loader.addURL(file.toURL())
                }

                // Read image name/id from CLI argument
                def imageNameArgument = target.hasProperty('imageNameArg') ? target.property('imageNameArg') : 'defaultName'

                // Run the vulnerabilities scan
                def imageScan = new ScanImage(imageNameArgument)
                imageScan.scanDockerImage()
            }
        }
    }

}
package com.scanImage.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin

class runScanImage implements Plugin<Project> {
    void apply(Project target) {
        target.task('scan') << {
            def imageScan = new ScanImage('java', '/home/Paul/')
            imageScan.scanDockerImage()
        }
    }
}
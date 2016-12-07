package com.testPlugin.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin

class testPluginCode implements Plugin<Project> {
    void apply(Project target) {
        target.task('hello') << {
			println "Hello World!"}
    }
}

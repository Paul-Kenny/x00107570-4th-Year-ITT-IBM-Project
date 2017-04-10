package com.scanImage.gradle

/**
 * Created by Paul on 3/31/17.
 * Class to build jar file description object found in the vulnerabilities database.
 */
class Jar {

    // Jar name and description
    String jarName, jarDesc

    // Jar CVE list
    def cveList = []

    // Jar constructor
    Jar(String jarName, String jarDesc) {
        this.jarName = jarName
        this.jarDesc = jarDesc
    }
}
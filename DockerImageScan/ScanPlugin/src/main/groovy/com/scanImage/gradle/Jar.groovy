package com.scanImage.gradle

/**
 * Created by Paul on 3/31/17.
 */
class Jar {

    String jarName, jarDesc
    def cveList = []


    Jar(String jarName, String jarDesc){
        this.jarName = jarName
        this.jarDesc = jarDesc
    }
}
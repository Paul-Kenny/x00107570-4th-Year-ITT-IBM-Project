package com.scanImage.gradle

/**
 * Created by Paul on 4/20/17.
 */
class DBCredentials {

    // Get DB username from environment variables
    def getUserName(){

        // Get username from environment variables
        def user = System.getenv("SCAN_USER")
        return user
    }

    // Get DB password from environment variables
    def getPassword(){

        // Get password from environment variables
        def password = System.getenv("SCAN_PASSWORD")
        return password
    }
}

package com.scanImage.gradle

import org.gradle.api.Project


/**
 * Created by Paul on 3/14/17.
 * Class to connect to and query the remote vulnerabilities database.
 */
import java.sql.*
import groovy.sql.*

class DBInterface {

    def sql
    def vulnerabilitiesList = []

    void connect(Project target) {

        // Resolve JDBC driver to establish DB connection
        def jdbcDriverConfig = target.getConfigurations().create('driver')
        target.getDependencies().add(jdbcDriverConfig.name, 'mysql:mysql-connector-java:6.0.6')
        URLClassLoader loader = GroovyObject.class.classLoader
        target.getConfigurations().driver.each { File file ->
            loader.addURL(file.toURL())
        }

        // Set connection username and password
        DBCredentials credentials = new DBCredentials()
        def username = credentials.getUserName()
        def password = credentials.getPassword()

        // Connect to database
        try {
            def db = [
                    url     : 'jdbc:mysql://jar-vul.crxuc0o6w3aw.us-west-2.rds.amazonaws.com:3306/jar_vul',
                    user    : username,
                    password: password,
                    driver  : 'com.mysql.cj.jdbc.Driver'
            ]
            sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

            println "Connected to DB"
            println "Querying database. Please wait..."

        } catch (SQLException ex) {
            println "No connection found!"
            println "SQLException: " + ex.getMessage()
            println "SQLState: " + ex.getSQLState()
            println "VendorError: " + ex.getErrorCode()
            throw ex
        }
    }

    // Query database for jar files found
    void queryDBForJar(List jarList) {

        for (String item : jarList) {

            try {
                sql.eachRow("select * from Jar where Jar.JAR_NAME = '" + item + "'") { row ->
                    String name = row.JAR_NAME
                    String jarDesc = row.JAR_DESC

                    // create jar object
                    def jar = new Jar(name, jarDesc)
                    // Add to vulnerabilities list
                    vulnerabilitiesList << jar
                }
            } catch (SQLException ex) {
                println "SQLException: " + ex.getMessage()
                println "SQLState: " + ex.getSQLState()
                println "VendorError: " + ex.getErrorCode()
            }
        }
    }

    // Query database for jar file vulnerabilities
    void queryDBForCVE(List jarList) {
        for (String item : jarList) {
            try {
                sql.eachRow("select * from Jar, CVE where Jar.JAR_NAME = '" + item + "' and CVE.JAR_NAME_CVE = '" + item + "'") { row ->
                    String name = row.JAR_NAME
                    String id = row.CVE_ID
                    String cveDesc = row.CVE_DESC
                    String cvss = row.CVSS_SCORE
                    String cvssFlag = row.CVSS_FLAG
                    String vector = row.ACCESS_VECTOR
                    String auth = row.AUTH
                    String impact = row.IMPACT_TYPE
                    String vulType = row.VUL_TYPE
                    String cweId = row.CWE_ID
                    String cweURL = row.CWE_LINK
                    String nvdURL = row.NVD_LINK

                    // create CVE object
                    def cve = new CVE(name, id, cveDesc, cvssFlag, vector, auth, impact, vulType, cweURL, nvdURL, cvss, cweId)
                    // Add CVE to appropriate Jar listing
                    for (Jar jarItem : vulnerabilitiesList) {
                        if (jarItem.jarName.equalsIgnoreCase(cve.jarName)) {
                            jarItem.cveList.add(cve)
                        }
                    }
                }
            } catch (SQLException ex) {
                println "SQLException: " + ex.getMessage()
                println "SQLState: " + ex.getSQLState()
                println "VendorError: " + ex.getErrorCode()
            }
        }
    }

    // Get vulnerabilities list
    def getVulnerabilitiesList(){
        return vulnerabilitiesList
    }

    // Close database connection
    void closeDB() {
        try {
            sql.close()
            println "Database connection closed!"
        } catch (SQLException ex) {
            println "Could not close connection!"
            println "SQLException: " + ex.getMessage()
            println "SQLState: " + ex.getSQLState()
            println "VendorError: " + ex.getErrorCode()
        }
    }
}
package com.scanImage.gradle

/**
 * Created by Paul on 3/14/17.
 */
import java.sql.*
import groovy.sql.*

class DBInterface {

    def sql

    void connect() {
        try {
            def db = [
                    url     : 'jdbc:mysql://jar-vul.crxuc0o6w3aw.us-west-2.rds.amazonaws.com:3306/jar_vul',
                    user    : 'paul',
                    password: 'paulk990099',
                    driver  : 'com.mysql.cj.jdbc.Driver'
            ]
            sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

            println("Connected to DB")
        } catch (SQLException ex) {
            println "No connection found!"
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            throw ex
        }
    }

    // Query database for jar files found
    void queryDBForJar(List jarList) {

        def queryFiles = jarList.size()

        for (String item : jarList) {
            ///change this
            println  (queryFiles + " jar files to query. \r")
            queryFiles--
            try {
                sql.eachRow("select * from Jar where Jar.JAR_NAME = '" + item + "'") { row ->
                    String name = row.JAR_NAME
                    String jarDesc = row.JAR_DESC

                    // create jar object
                    def jar = new Jar(name, jarDesc)
                    ScanImage.vulList << jar
                }
            } finally {

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
                    cve.addCVEToVulList(cve)
                }
            } finally {

            }
        }
    }

    // Close database connection

    void closeDB() {
        try {
            sql.close()
            println "Database connection closed!"
        } catch (SQLException ex) {
            println "Could not close connection!"
            System.out.println("SQLException: " + ex.getMessage())
            System.out.println("SQLState: " + ex.getSQLState())
            System.out.println("VendorError: " + ex.getErrorCode())
        }
    }
}
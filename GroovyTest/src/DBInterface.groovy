/**
 * Created by Paul on 3/14/17.
 */
import java.sql.*

class DBInterface {

    Connection conn = null
    Statement stmt = null
    ResultSet rs = null

    void connect() {
        try {
            // Create database connection
            Class.forName("com.mysql.jdbc.Driver")
            conn = DriverManager.getConnection("jdbc:mysql://jar-vul.crxuc0o6w3aw.us-west-2.rds.amazonaws.com:3306/jar_vul", "paul", "paulk990099")
            println("Connected to DB")
        } catch (SQLException e) {
            println "No connection found!"
        }
    }

    void queryDBForJar(List jarList) {
        for (String item : jarList) {

            println item

            String query = "select * from Jar where Jar.JAR_NAME = '" + item + "'"

            try {
                stmt = conn.createStatement()
                rs = stmt.executeQuery(query)

                while (rs.next()) {
                    String name = rs.getString("JAR_NAME")
                    String jarDesc = rs.getString("JAR_DESC")

                    println("Jar: " +
                            "\nName: " + name +
                            "\nDescription: " + jarDesc)

                    // create jar object
                    def jar = new Jar(name, jarDesc)
                    ScanImage.vulList << jar
                }
            } catch (SQLException e) {

            }
        }
    }


    void queryDB(List jarList) {
        for (String item : jarList) {

            println item

            String query = "select * from Jar, CVE where Jar.JAR_NAME = '" + item + "' and CVE.JAR_NAME_CVE = '" + item + "'"

            try {
                stmt = conn.createStatement()
                rs = stmt.executeQuery(query)

                while (rs.next()) {
                    String name = rs.getString("JAR_NAME")
                    String jarDesc = rs.getString("JAR_DESC")
                    String id = rs.getString("CVE_ID")
                    String cveDesc = rs.getString("CVE_DESC")
                    String cvss = rs.getDouble("CVSS_SCORE")
                    String cvssFlag = rs.getString("CVSS_FLAG")
                    String vector = rs.getString("ACCESS_VECTOR")
                    String auth = rs.getString("AUTH")
                    String impact = rs.getString("IMPACT_TYPE")
                    String vulType = rs.getString("VUL_TYPE")
                    String cweId = rs.getInt("CWE_ID")
                    String cweURL = rs.getString("CWE_LINK")
                    String nvdURL = rs.getString("NVD_LINK")

                    println("Jar: " +
                            "\nName: " + name +
                            "\nDescription: " + jarDesc +
                            "\nCVE id : " + id +
                            "\nCVE Description: " + cveDesc +
                            "\nCVSS Score: " + cvss +
                            "\nCVSS Flag: " + cvssFlag +
                            "\nVector: " + vector +
                            "\nAuthentication: " + auth +
                            "\nImpact: " + impact +
                            "\nVulnerability Type: " + vulType +
                            "\nCWE ID: " + cweId +
                            "\nCWE URL: " + cweURL +
                            "\nNVD URL: " + nvdURL + "\n")

                    // create CVE object
                    def cve = new CVE(name, id, cveDesc, cvssFlag, vector, auth, impact, vulType, cweURL, nvdURL, cvss, cweId)
                    cve.addCVEToVulList(cve)
                }
            } catch (SQLException e) {

            }
        }
    }

    void closeDB(){
        try{
            conn.close()
            println "Database connection closed!"
        } catch (SQLException e){
            println "Could not close connection!"
        }
    }
}





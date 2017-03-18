/**
 * Created by Paul on 3/14/17.
 */
import java.sql.*

class DBInterface {

    void connect() {
        Connection conn = null
        Class.forName("com.mysql.jdbc.Driver")
        conn = DriverManager.getConnection("jdbc:mysql://jar-vul.crxuc0o6w3aw.us-west-2.rds.amazonaws.com:3306/jar_vul", "paul", "paulk990099")
        println("Connected to DB")

        Statement stmt = null
        ResultSet rs = null


        String query = "select * from Jar, CVE where JAR_NAME = 'ca-certificates-java'"

        stmt = conn.createStatement()
        rs = stmt.executeQuery(query)

        while(rs.next()){
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
                    "CVE Description: " + cveDesc +
                    "\nCVSS Score: " + cvss +
                    "\nCVSS Flag: " + cvssFlag +
                    "\nVector: " + vector +
                    "\nAuthentication: " + auth +
                    "\nImpact: " + impact +
                     "\nVulnerability Type: " + vulType +
                      "\nCWE ID: " + cweId +
                      "\nCWE URL: " + cweURL +
                       "\nNVD URL: " + nvdURL + "\n")
        }




    }




}


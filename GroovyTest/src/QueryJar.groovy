/**
 * Created by Paul on 3/18/17.
 */
import java.sql.*

class QueryJar {

    static void query(DriverManager conn){
        Statement stmt = null
        ResultSet rs = null

        String query = "select * from CVE where JAR_NAME_CVE = 'ca-certificates-java'"

        stmt = conn.createStatement()
        rs = stmt.executeQuery(query)

        while(rs.next()){
            String id = rs.getString("CVE_ID")
            String name = rs.getString("JAR_NAME_CVE")
            String cvss = rs.getDouble("CVSS_SCORE")

            println("CVE id : " + id + "\nJar Name: " + name + "\nCVSS Score: " + cvss)
        }
    }


}

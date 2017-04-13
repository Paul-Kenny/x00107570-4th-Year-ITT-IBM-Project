import com.scanImage.gradle.CVE
import com.scanImage.gradle.Jar
import spock.lang.Specification

/**
 * Created by Paul on 4/11/17.
 */
class JarTests extends Specification{

    def "Create Jar object"() {

        setup:
        // Create fields for Jar
        def jarName = "Test Jar"
        def jarDesc = "Test Desc"

        when:
        Jar jar = new Jar(jarName, jarDesc)

        then:
        jar.jarName == "Test Jar"
        jar.jarDesc == "Test Desc"

    }

    def "Add CVE object to Jar object CVE list"() {

        setup:
        // Create fields for Jar
        def jarName = "Test Jar"
        def jarDesc = "Test Desc"

        // Create fields for CVE
        def cveId = "CVE_1000_1000"
        def cveDesc = "Test Desc"
        def cveScore = "2.5"
        def cvssFlag = "MEDIUM"
        def accessVector = "Test Vector"
        def auth = "Test Authentication"
        def impactType = "Test Type"
        def vulType = "Test Vulnerability"
        def cweId = "Test Id"
        def cweUrl = "Test CWE Url"
        def nvdUrl = "Test NVD Url"

        when:
        // Create Jar object
        Jar jar = new Jar(jarName, jarDesc)
        // Create CVE object
        CVE cveNew = new CVE(jarName, cveId, cveDesc, cvssFlag, accessVector, auth, impactType, vulType, cweUrl, nvdUrl, cveScore, cweId, )

        // Add new CVE to CVE array in Jar object
        jar.cveList.add(cveNew)

        then:
        // Test to see if Jar object has been created successfully...
        jar.jarName == "Test Jar"
        jar.jarDesc == "Test Desc"

        // ...and CVE object has been added to cveList successfully
        for (CVE cve : jar.cveList) {
            cve.jarName == "Test Jar"
            cve.cveDesc == "Test Desc"
            cve.cveScore == "2.5"
            cve.cvssFlag == "MEDIUM"
            cve.accessVector == "Test Vector"
            cve.auth == "Test Authentication"
            cve.impactType == "Test Type"
            cve.vulType == "Test Vulnerability"
            cve.cweId == "Test Id"
            cve.cweUrl == "Test CWE Url"
            cve.nvdUrl == "Test NVD Url"
        }

    }


}

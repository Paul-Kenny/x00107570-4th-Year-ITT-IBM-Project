import com.scanImage.gradle.CVE
import spock.lang.Specification

/**
 * Created by Paul on 4/11/17.
 */
class CVETests extends Specification{

    def "Create CVE object"() {

        setup:
        // Create fields for CVE
        def jarName = "Test Jar"
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
        // Create CVE object
        CVE cve = new CVE(jarName, cveId, cveDesc, cvssFlag, accessVector, auth, impactType, vulType, cweUrl, nvdUrl, cveScore, cweId, )

        then:
        // Test to see if CVE object has been created successfully
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

/**
 * Created by Paul on 3/30/17.
 */
class CVE {

    String jarName, cveId, cveDesc, cvssFlag, accessVector, auth, impactType, vulType, cweUrl, nvdUrl, cveScore, cweId

    CVE(String jarName, String cveId, String cveDesc, String cvssFlag, String accessVector, String auth, String impactType, String vulType, String cweUrl, String nvdUrl, String cveScore, String cweId){
        this.jarName = jarName
        this.cveId = cveId
        this.cveDesc = cveDesc
        this.cveScore = cveScore
        this.cvssFlag = cvssFlag
        this.accessVector = accessVector
        this.auth = auth
        this.impactType = impactType
        this.vulType = vulType
        this.cweId = cweId
        this.cweUrl = cweUrl
        this.nvdUrl = nvdUrl
    }

    // Make HTML block?
    void addCVEToVulList(CVE cve){
        for (Jar item : ScanImage.vulList) {
            println "ITEM>JAR>NAME" + item.jarName
            println "CVE>JARNAME" + cve.jarName
            if(item.jarName == cve.jarName){
                item.cveList.add(cve)
            }
        }
    }
}

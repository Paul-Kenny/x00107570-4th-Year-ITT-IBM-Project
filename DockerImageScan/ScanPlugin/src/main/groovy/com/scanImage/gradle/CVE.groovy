package com.scanImage.gradle

/**
 * Created by Paul on 3/31/17.
 */
class CVE {

    // CVE metrics
    String jarName, cveId, cveDesc, cvssFlag, accessVector, auth, impactType, vulType, cweUrl, nvdUrl, cveScore, cweId

    // CVE constructor
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

    // Add the CVE to the vulnerabilities array
    void addCVEToVulList(CVE cve){
        for (Jar item : ScanImage.vulList) {
            if(item.jarName.equalsIgnoreCase(cve.jarName)){
                item.cveList.add(cve)
            }
        }
    }
}
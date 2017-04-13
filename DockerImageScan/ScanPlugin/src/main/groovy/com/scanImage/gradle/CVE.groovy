package com.scanImage.gradle

/**
 * Created by Paul on 3/31/17.
 * Class to build CVE object found in the Docker image.
 */
class CVE {

    // CVE metrics
    String jarName, cveId, cveDesc, cvssFlag, accessVector, auth, impactType, vulType, cweUrl, nvdUrl, cveScore, cweId

    // CVE constructor
    CVE(String jarName, String cveId, String cveDesc, String cvssFlag, String accessVector, String auth, String impactType, String vulType, String cweUrl, String nvdUrl, String cveScore, String cweId) {
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

}
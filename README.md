# Gradle plugin - Docker Image Security Vulnerabilities Scan


### Project Overview:

A Java application is wrapped in a Docker Image prior to deployment as a Docker Container. The source code has been scanned for known security vulnerabilities as part of the Jenkins pipeline. However, this process does not identify whether the Docker Image contains security vulnerabilities. 

To address this issue, this application was developed to ensure that the developer is aware of security vulnerabilities present in the Docker Image prior to deployment.

The Gradle plugin will allow a developer to run a security vulnerabilities scan on a Docker Image. This plugin will scan the chosen image for Java Framework dependencies, in the form of JAR files, and cross reference these dependencies with a database of known security vulnerabilities.

If security vulnerabilities are found, the plugin will relay the results back to the developer as a HTML report. This report will contain the security vulnerabilities, in the form of Common Vulnerabilities & Exposures (CVE) entries, for each affected dependency found.

The system is a three tier architecture using an AWS RDS MySQL instance as the data layer, a Gradle Plugin to scan the Docker Image and query the database, and a HTML based results interface.

### Technologies, Platforms, Libraries and Frameworks Used:

 * Gradle: The application is built as a Gradle Plugin and designed for execution on a Linux based OS.
 * Groovy: The plugin is written in Groovy. While the Gradle build scripts are written in a domain specific language based on Groovy.
 * SQL: SQL is used to cross-reference JAR files found in the chosen Docker Image with the security vulnerabilities database.
 * Docker: Docker Images will be scanned by this plugin to find JAR files with security vulnerabilities.
 * Cobertura: Executed at compile time to calculate percentage of source code accessed during unit tests.
 * Apache Commons Compress: Utilised in the inspection of tarball and JAR files during the scanning process.
 * Connector/J: Connects the plugin to the remote security vulnerabilities database.
 * Spock: Testing and specification framework used to peform unit tests.
 * AWS RDS MySQL Database: Cloud based database instance hosting the security vulnerabilities database.

### AWS RDS MySQL Database:

* Hostname: jar-vul.crxuc0o6w3aw.us-west-2.rds.amazonaws.com
* Port: 3306
* Username: Paul
* Password: paulk990099

JAR Query:
```
select * from Jar where Jar.JAR_NAME = '<jarName>';
```

CVE Query:
```
select * from Jar, CVE where Jar.JAR_NAME = '<jarName>' and CVE.JAR_NAME_CVE = '<jarName>';
```

The database username and password are not hardcoded into the plugin, rather they are to be set as environment variables on the users local machine. 
The plugin will get these credentials from the local system at runtime. Set the credentials as environment variables:
```
export SCAN_USER=paul
export SCAN_PASSWORD=paulk990099
```

### Getting Started

Once the plugin is cloned down it will need to be compiled by Gradle.

Navigate to the plugin directory, build the plugin and upload the archives:
```
 cd DockerImageScan/ScanPlugin
 gradle clean build
 gradle uploadArchives
```
Once the plugin is built it can be stored in a repository for future use or run locally by a developer.

Running locally:
``` 
 cd DockerImageScan/user
 gradle scan -PimageNameArg="<name of Docker Image stored on local machine>"
```
Once the "scan" has been executed on a local Docker Image, the plugin will generate a HTML security vulnerabilities report. This report will launch automatically on the developers default browser. The report is stored in the "DockerImageScan/user/Report/Vulnerabilities_Reports/" directory. The report name will take the format "imageName(timestamp).html". If the html report is to be copied to another directory it is recommended that the accompanying CSS directory be copied also.

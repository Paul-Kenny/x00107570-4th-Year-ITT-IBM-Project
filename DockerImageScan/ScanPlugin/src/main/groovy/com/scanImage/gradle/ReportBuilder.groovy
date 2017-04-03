package com.scanImage.gradle

/**
 * Created by Paul on 4/3/17.
 */
import groovy.xml.MarkupBuilder

class ReportBuilder {

    def build(){
        StringWriter writer = new StringWriter()
        def build = new MarkupBuilder(writer)
        build.html{
            head{
                meta('charset':'utf-8')
                meta('name':'viewport', 'content':'width=device-width, initial-scale=1')
                title("Docker Jar Vulnerabilities")
                meta('name':'description', 'content':'Docker Image Scan Security Vulnerabilities Results')

                link('rel':'stylesheet', 'type':'text/css', 'href':'css/style.css')
                script('','src':'https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js')
                script('$(document).ready(function(){\n' +
                        '                $("#accordianmenu p").click(function(){\n' +
                        '\t\t          $("#accordianmenu ul ul").slideUp();\n' +
                        '\t\t          if(!$(this).next().is(":visible"))\n' +
                        '\t\t          {\n' +
                        '                      $(this).next().slideDown();\n' +
                        '\t\t          }\n' +
                        '\t           });\n' +
                        '            });')
            }
            body{
                nav('id':'header'){
                    div('class':'pull-left fnav'){
                        p('IBM'){
                            a('style':'color: #fcac45;','Docker Image Vulnerabilities Scan Results')
                        }
                    }
                }
                div('id':'secVul'){
                    div('class':'section-title center'){
                        h2(){
                            strong('Security Vulnerabilities')
                        }


                        // Dynamic content start
                        for (Jar jar : ScanImage.vulList) {

                            div('class': 'jar') {
                                div('id': 'accordianmenu') {
                                    ul() {
                                        li() {
                                            p('class': 'jar_name') {
                                                strong(jar.jarName)
                                            }
                                            ul() {
                                                li() {
                                                    a(jar.jarDesc)
                                                }
                                            }
                                        }
                                    }
                                }

                                for(CVE cve : jar.cveList) {
                                    div('id': 'accordianmenu') {
                                        ul() {
                                            li() {
                                                p() {
                                                    strong(cve.cveId)
                                                }
                                                ul() {

                                                    li() {
                                                        span('class': 'metric') {
                                                            a('CVE ID: ')
                                                        }
                                                        span('class': 'result') {
                                                            a('href': cve.nvdUrl, 'target': '_blank', cve.cveId)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('CVE Description: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.cveDesc)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('CVSS Score: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.cveScore)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('CVSS Flag: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.cvssFlag)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('Access Vector: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.accessVector)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('Authentication: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.auth)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('Impact Type: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.impactType)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('Vulnerability Type: ')
                                                        }
                                                        span('class': 'result') {
                                                            a(cve.vulType)
                                                        }
                                                    }
                                                    li() {
                                                        span('class': 'metric') {
                                                            a('CWE ID: ')
                                                        }
                                                        span('class': 'result') {
                                                            a('href': cve.cweUrl, 'target': '_blank', cve.cweId)
                                                        }
                                                    }


                                                }
                                            }
                                        }
                                    }// CVE End
                                }

                            }// Jar end
                        }





                        // Dynamic content end
                    }
                }
            }
        }
        return writer.toString()
    }

}


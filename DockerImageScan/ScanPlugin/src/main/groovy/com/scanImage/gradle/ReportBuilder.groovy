package com.scanImage.gradle

/**
 * Created by Paul on 4/3/17.
 * Class to dynamically build the HTML security vulnerabilities report.
 */
import groovy.xml.MarkupBuilder

class ReportBuilder {

    // Build the HTML vulnerabilities report
    def build(String imageName) {

        StringWriter writer = new StringWriter()
        def build = new MarkupBuilder(writer)

        build.html { // Static markup start
            head {
                meta('charset': 'utf-8')
                meta('name': 'viewport', 'content': 'width=device-width, initial-scale=1')
                title("Docker Jar Vulnerabilities")
                meta('name': 'description', 'content': 'Docker Image Scan Security Vulnerabilities Results')

                link('rel': 'stylesheet', 'type': 'text/css', 'href': 'css/style.css')
                script('', 'src': 'https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js')
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
            body {
                nav('id': 'header') {
                    div('class': 'pull-left fnav') {
                        p('IBM') {
                            a('style': 'color: #838b8b;', 'Docker Image Vulnerabilities Scan Results')
                        }
                    }
                }
                div('id': 'secVul') {
                    div('id': 'row', 'class': 'item') {
                        div('class': 'section-title center') {
                            h2() {
                                strong(imageName + ' Image Security Vulnerabilities')
                            } // Static markup end
                            if (ScanImage.vulList.size() != 0) { // Dynamic markup start

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

                                        for (CVE cve : jar.cveList) {
                                            div('id': 'accordianmenu') {
                                                ul() {
                                                    li() {
                                                        if (cve.cvssFlag == "LOW") {
                                                            p('class': 'low') {
                                                                strong(cve.cveId)
                                                            }
                                                        } else if (cve.cvssFlag == "MEDIUM") {
                                                            p('class': 'medium') {
                                                                strong(cve.cveId)
                                                            }
                                                        } else {
                                                            p('class': 'high') {
                                                                strong(cve.cveId)
                                                            }
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
                                                                if (cve.cvssFlag == "LOW") {
                                                                    span('class': 'result') {
                                                                        a('class': 'low', cve.cveScore)
                                                                    }
                                                                } else if (cve.cvssFlag == "MEDIUM") {
                                                                    span('class': 'result') {
                                                                        a('class': 'medium', cve.cveScore)
                                                                    }
                                                                } else {
                                                                    span('class': 'result') {
                                                                        a('class': 'high', cve.cveScore)
                                                                    }
                                                                }
                                                            }
                                                            li() {
                                                                span('class': 'metric') {
                                                                    a('CVSS Flag: ')
                                                                }
                                                                if (cve.cvssFlag == "LOW") {
                                                                    span('class': 'result') {
                                                                        a('class': 'low', cve.cvssFlag)
                                                                    }
                                                                } else if (cve.cvssFlag == "MEDIUM") {
                                                                    span('class': 'result') {
                                                                        a('class': 'medium', cve.cvssFlag)
                                                                    }
                                                                } else {
                                                                    span('class': 'result') {
                                                                        a('class': 'high', cve.cvssFlag)
                                                                    }
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
                            } // Dynamic markup end
                            else {
                                h2() {
                                    strong('No Security Vulnerabilities Found!!')
                                }
                            }
                        } // Static markup start
                    }
                }
            }
        } // Static markup end
        return writer.toString()
    }
}

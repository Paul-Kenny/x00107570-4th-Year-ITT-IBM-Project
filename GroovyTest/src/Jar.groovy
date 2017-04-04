/**
 * Created by Paul on 3/30/17.
 */
class Jar {

    // Jar name and description
    String jarName, jarDesc

    // Jar CVE list
    def cveList = []

    // Jar constructor
    Jar(String jarName, String jarDesc){
        this.jarName = jarName
        this.jarDesc = jarDesc
    }
}

import com.scanImage.gradle.ReportBuilder
import spock.lang.Specification

import java.text.SimpleDateFormat

/**
 * Created by Paul on 4/13/17.
 */
class ReportBuilderTest extends Specification{

    def "Test if report is generated"(){
        setup:

        // Create image name
        def imageName = "Test_Image"
        // Create empty vulnerabilities list
        def vulnerabilitiesList = []

        // Make temporary test directory
        new File("./src/ReportBuilderTestDir/").mkdir()

        // Build the html report
        ReportBuilder report = new ReportBuilder(vulnerabilitiesList)
        def html = report.build(imageName)

        when:
        // Create test state
        def result = true

        // Add html markup to the output report
        def reportName = "./src/ReportBuilderTestDir/" + imageName + "(" + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(new Date()) + ").html"
        def index = new File(reportName)
        index << html


        // Change test state if directory does not exist (it has been removed)
        if(index.exists()){
            result = true
        }

        then:
        // Check if test is passed
        result == true

        cleanup:
        // Remove test directories
        String removeDir = "rm -rf ./src/ReportBuilderTestDir/"
        removeDir.execute().waitFor()
    }
}

import com.scanImage.gradle.DockerImage
import spock.lang.Specification

/**
 * Created by Paul on 4/13/17.
 */
class DockerImageTest extends Specification{

    def "Test if Docker image exists"(){
        setup:
        // Setup to see if Docker image exists on system
        def inspectImage = new DockerImage()

        when:
        // Declare test state
        def result

        // Test to see if image exists. Returns an empty error message if image exists
        def inspectError = inspectImage.testIfImageExists('java')

        // Test if error message is empty
        if(inspectError.isEmpty()){
            result = true
        }

        then:
        // Check if test is passed
        result == true
    }

    def "Test if Docker image does not exist"(){
        setup:
        // Setup to see if Docker image exists on system
        def inspectImage = new DockerImage()

        when:
        // Declare test state
        def result

        // Test to see if image exists. Returns an empty error message if image exists
        def inspectError = inspectImage.testIfImageExists('NotAnImage')

        // Test if error message is contains correct message
        if(inspectError.contains("Error: No such image, container or task: " ) ){
            result = true
        }

        then:
        // Check if test is passed
        result == true
    }
}

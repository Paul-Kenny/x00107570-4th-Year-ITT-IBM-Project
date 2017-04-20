import com.scanImage.gradle.DBCredentials
import spock.lang.Specification

/**
 * Created by Paul on 4/20/17.
 */
class DBCredentialsTests extends Specification{

    def "Pull username and password from environment variables"() {

        setup:
        // Declare expected username result
        def expUsername = "paul"
        // Declare expected password result
        def expPassword = "paulk990099"

        // Create DBCredentials object
        DBCredentials credentials = new DBCredentials()

        when:
        // Call getUserName method
        def username = credentials.getUserName()

        // Call getPassword method
        def password = credentials.getPassword()

        then:
        // Test to see if username and password match
        username == expUsername
        password == expPassword
    }
}

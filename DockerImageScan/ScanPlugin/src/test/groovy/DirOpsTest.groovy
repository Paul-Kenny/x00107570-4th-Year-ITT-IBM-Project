/**
 * Created by Paul on 4/11/17.
 */


import com.scanImage.gradle.DirectoryOperations
import spock.lang.Specification

class DirOpsTest extends Specification{

    def "Make temporary directory"() {

        setup:
        DirectoryOperations dirOps = new DirectoryOperations()

        when:
        def result = dirOps.makeDir('/home/Paul/')
        System.getProperty("user.dir")
        then:
        result == '/home/Paul/Temp/'
    }

    def "Remove temporary directory"() {
        setup:
        DirectoryOperations dirOps = new DirectoryOperations()

        when:
        def newDir = new File('/home/Paul/Temp')
        dirOps.removeDir('/home/Paul/Temp')
        def result = false
        if(!newDir.exists()){
            result = true
        }

        then:
        result == true
    }
}
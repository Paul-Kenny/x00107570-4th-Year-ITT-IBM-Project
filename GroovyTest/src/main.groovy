/**
 * Created by Paul on 25-Jan-17.
 */

//Standard image scan
//imageScan = new DockerImageScan('java', '/home/Paul/')
//imageScan.scanImage()

//New image scan
imageScan = new ScanImage('java', '/home/Paul/')
imageScan.scanDockerImage()
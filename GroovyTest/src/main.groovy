/**
 * Created by Paul on 25-Jan-17.
 */
//test = new TestClass('java', '/home/Paul/')
//test.start('java', '/home/Paul/')

//tar -xf /home/Paul/Temp/Temp/fbebd615ed02d0a75bea03aeea50fbc7e65b8b1c26ff5f261293c2c38b54fc7b/layer.tar usr/share/java/java-atk-wrapper.jar

imageScan = new DockerImageScan('java', '/home/Paul/')
imageScan.scanImage()
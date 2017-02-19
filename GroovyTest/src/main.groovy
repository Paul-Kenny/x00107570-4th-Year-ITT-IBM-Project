/**
 * Created by Paul on 25-Jan-17.
 */
test = new TestClass()
//test.basicTarRead('/home/Paul/java.tar')
//test.basicTarRead('/home/Paul/java/fbebd615ed02d0a75bea03aeea50fbc7e65b8b1c26ff5f261293c2c38b54fc7b/layer.tar')
//test.tarRead('/home/Paul/ubuntu.tar')
//"docker images".execute()
//"docker save ubuntu > /home/Paul/ubuntu.tar".execute()
//"mkdir /home/Paul/TempTar".execute()
//"tar -xvf /home/Paul/hw.tar -C /home/Paul/TempTar".execute()
//"rm -rf /home/Paul/TempTar".execute()
//test.executeOnShell('ls',' -la')
//"ls".execute()
//test.untarImage('hw', '/home/Paul/')
//test.readTempTar('/home/Paul/TempTar2')

//test.dockerTar('hello-world', '/home/Paul/TempTar/')
test.start('java', '/home/Paul/')
//test.untarImage('java', '/home/Paul/Temp/', '/home/Paul/Temp/Temp/')
//test.readTempTar('/home/Paul/Temp/Temp')
//test.basicJarRead('/home/Paul/Temp/Temp/Temp/usr/share/java/java-atk-wrapper.jar')

//tar -xf /home/Paul/Temp/Temp/fbebd615ed02d0a75bea03aeea50fbc7e65b8b1c26ff5f261293c2c38b54fc7b/layer.tar usr/share/java/java-atk-wrapper.jar

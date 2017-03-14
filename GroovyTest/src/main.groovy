/**
 * Created by Paul on 25-Jan-17.
 */

//Image scan onto faster mounted ram disk
//mountRamDisk = new DirBuilder('/ramdisk')
//mountRamDisk.makeDisk()
//mountRamDisk.mountDisk()
//mountRamDisk.unmountDisk()
//mountRamDisk.deleteDisk()

//imageScan = new DockerImageScan('java', '/ramdisk/')
//imageScan.scanImage()



//Standard image scan
imageScan = new DockerImageScan('java', '/home/Paul/')
imageScan.scanImage()





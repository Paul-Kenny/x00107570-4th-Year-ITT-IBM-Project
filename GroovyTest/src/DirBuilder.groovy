/**
 * Created by Paul on 3/6/17.
 */
class DirBuilder {

    String disk

    DirBuilder(String diskIn){
        disk = diskIn
    }

    void makeDisk(){
        String makeDisk = "xterm -e sudo mkdir " + disk
        makeDisk.execute().waitFor()

    }
    void mountDisk(){
        String mount = "xterm -e sudo mount -t tmpfs -o size=2048m tmpfs " + disk
        println mount
        mount.execute().waitFor()
    }
    void unmountDisk(){
        String unmount = "xterm -e sudo umount " + disk
        println unmount
        unmount.execute().waitFor()
    }
    void deleteDisk(){
        String deleteDisk = "xterm -e sudo rm -rf " + disk
        deleteDisk.execute().waitFor()

    }

}

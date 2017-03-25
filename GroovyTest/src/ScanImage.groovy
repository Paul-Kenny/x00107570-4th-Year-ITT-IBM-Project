/**
 * Created by Paul on 3/25/17.
 */
class ScanImage {

    String tarballDir, untarDir, jarDir, imageName


    ScanImage(String imageNameIn, String tempDir) {

        // Temporary directories object
        def mkDir = new DirectoryOperations()

        // Make temporary directories
        tarballDir = mkDir.makeDir(tempDir)
        untarDir = mkDir.makeDir(tarballDir)
        jarDir = mkDir.makeDir(untarDir)
        imageName = imageNameIn
    }

    // Start the scan process
    void scanDockerImage() {

        def tarball = new TarballOperations(jarDir)
        // Save the docker image tarball to the tarballDir
        tarball.dockerTar(imageName, tarballDir)

        // Unarchive the image tarball untarDir
        tarball.untarImage(imageName, tarballDir, untarDir)

        // Read all directories in untar
        tarball.readTempTar(untarDir)

        // Remove temporary directory
        def rmDir = new DirectoryOperations()
        rmDir.removeDir(tarballDir)

        def jarFile = new JarFileOperations()


        // Query the database
        def connection = new DBInterface()
        connection.connect()
        connection.queryDB(jarFile.jarList)
        connection.closeDB()
    }

}

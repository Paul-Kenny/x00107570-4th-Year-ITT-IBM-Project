import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by Paul on 25-Jan-17.
 */

class HelloWorld {
    /*testcode
    void hello () {
        println("Hello")
    }*/

    /*void read (outerTarPath) {

        String contents = new File(outerTarPath).text
        println(contents);
    }*/

    /*void matcherTest(){
        String text    =
                "dbbeac75e0b4ce31f4f8d7tar4fb40657b346bc90f270dcc6c2caf02540ff102/layer.tax";

        String patternString =  "^.*\\.(tar)\$";

        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(text);
        boolean matches = matcher.matches();
        if(matches == true){
            System.out.println("Yay!")
        }
        else{
            System.out.println("Nah!")
        }

    }*/

    //Read and list files in tar
    void basicTarRead(String tarPath){
        /* Read TAR File into TarArchiveInputStream */
        TarArchiveInputStream myTarFile=new TarArchiveInputStream(new FileInputStream(new File(tarPath)));
        /* Read individual TAR file */
        TarArchiveEntry entry = null;
        String individualFiles;
        int offset;
        FileOutputStream outputFile=null;
        /* While loop to read every single entry in TAR file */
        while ((entry = myTarFile.getNextTarEntry()) != null) {
            /* Get the name of the file */
            individualFiles = entry.getName();
            /* SOP statement to check progress */
            System.out.println("File Name in TAR File is: " + individualFiles);
        }
        /* Close TarAchiveInputStream */
        myTarFile.close();
    }

    //Read list of files in tar
    //Locate inner tars and read list of files in them
    void tarRead(String tarPath){
        /* Read TAR File into TarArchiveInputStream */
        TarArchiveInputStream myTarFile=new TarArchiveInputStream(new FileInputStream(new File(tarPath)));
        /* Read individual TAR file */
        TarArchiveEntry entry = null;
        String individualFiles;
        int offset;
        FileOutputStream outputFile=null;
        /* While loop to read every single entry in TAR file */
        while ((entry = myTarFile.getNextTarEntry()) != null) {
            /* Get the name of the file */
            individualFiles = entry.getName();
            /* SOP statement to check progress */
            System.out.println("File Name in TAR File is: " + individualFiles);
            /*Test for tar regex*/
            String patternString =  "^.*\\.(tar)\$";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(individualFiles);
            boolean matches = matcher.matches();

            //If statement to start new tar search
            if(matches == true){
                System.out.println("Content of " + tarPath + "/" + individualFiles );
                outerInnerTarRead(tarPath, individualFiles);
                //read(tarPath, individualFiles);
            }
            else{
                System.out.println("Nope");
            }
        }
        /* Close TarAchiveInputStream */
        myTarFile.close();
    }

    void outerInnerTarRead(String outerTarPath, String innerTarPath){
        System.out.println("******************************************************************");
        /* Read TAR File into TarArchiveInputStream */
        TarArchiveInputStream myTarFile=new TarArchiveInputStream(new FileInputStream(new File(outerTarPath + "/" + innerTarPath)));
        /* Read individual TAR file */
        TarArchiveEntry entry = null;
        String individualFiles;
        int offset;
        FileOutputStream outputFile=null;
        /* While loop to read every single entry in TAR file */
        while ((entry = myTarFile.getNextTarEntry()) != null) {
            /* Get the name of the file */
            individualFiles = entry.getName();
            /* Some SOP statements to check progress */
            System.out.println("File Name in TAR File is: " + individualFiles);
        }
        /* Close TarAchiveInputStream */
        myTarFile.close();
        System.out.println("******************************************************************");
    }

    void unTar(){
            /* Read TAR File into TarArchiveInputStream */
            TarArchiveInputStream myTarFile=new TarArchiveInputStream(new FileInputStream(new File('C:/Users/Paul/Desktop/Tars/hw.tar')));
            /* To read individual TAR file */
            TarArchiveEntry entry = null;
            String individualFiles;
            int offset;
            FileOutputStream outputFile=null;
            /* Create a loop to read every single entry in TAR file */
            while ((entry = myTarFile.getNextTarEntry()) != null) {
                /* Get the name of the file */
                individualFiles = entry.getName();
                /* Get Size of the file and create a byte array for the size */
                byte[] content = new byte[(int) entry.getSize()];
                offset=0;
                /* Some SOP statements to check progress */
                System.out.println("File Name in TAR File is: " + individualFiles);
                System.out.println("Size of the File is: " + entry.getSize());
                System.out.println("Byte Array length: " + content.length);
                /* Read file from the archive into byte array */
                myTarFile.read(content, offset, content.length - offset);
                /* Define OutputStream for writing the file */
                outputFile=new FileOutputStream(new File(individualFiles));
                /* Use IOUtiles to write content of byte array to physical file */
                IOUtils.write(content,outputFile);
                /* Close Output Stream */
                outputFile.close();
            }
            /* Close TarAchiveInputStream */
            myTarFile.close();
    }



}

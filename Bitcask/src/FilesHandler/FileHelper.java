package FilesHandler;

import java.io.*;
import Entries.BitCaskEntry;

public class FileHelper {

    private RandomAccessFile activeFile;
    private String activeFilePath = "src/Storage/active.bin";
    private long nextFilePosition; // next available file position for writing
    static private int fileCounter = 0;



    private void initActiveFile(String filePath) throws IOException {
        this.activeFile = new RandomAccessFile(filePath,"rw");
        this.nextFilePosition = this.activeFile.length();
    }


    private void createNewActiveFile() throws IOException {
        this.activeFile.close();
        this.activeFilePath = activeFilePath+"\\"+fileCounter++;
        System.out.println("create new active File "+ this.activeFilePath);
        initActiveFile(this.activeFilePath);
    }

    public FileHelper() throws IOException {
            initActiveFile(this.activeFilePath);
    }


    public String getFileId(){
       return this.activeFilePath;
    }

    public long writeToActiveFile(BitCaskEntry entry) throws IOException {
        long filePosition = this.nextFilePosition;

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);


        objectStream.writeObject(entry);
        byte[] serializedEntry = byteStream.toByteArray();

        // Write the serialized entry to the file
        this.activeFile.seek(nextFilePosition);
        this.activeFile.write(serializedEntry);

        // Update nextFilePosition for the next write
        this.nextFilePosition = this.activeFile.getFilePointer();
        return filePosition;
    }




}

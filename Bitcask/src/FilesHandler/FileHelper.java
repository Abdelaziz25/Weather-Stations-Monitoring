package FilesHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Entries.BitCaskEntry;


public class FileHelper {

    private final int MAX_SIZE = 500;

    private RandomAccessFile activeFile;
    private String activeFilePath = "src/Storage/activeFile";
    private long nextFilePosition; // next available file position for writing
    static private int fileCounter = 0;

    private boolean debug = false;

    public FileHelper() throws IOException {
        initActiveFile(this.activeFilePath);
    }


    private void initActiveFile(String filePath) throws IOException {
        this.activeFile = new RandomAccessFile(filePath,"rw");
        this.nextFilePosition = this.activeFile.length();
    }


    private void createNewActiveFile() throws IOException {
        this.activeFile.close();
        this.activeFilePath = activeFilePath+String.valueOf(fileCounter++);
        System.out.println("create new active File "+ this.activeFilePath);
        initActiveFile(this.activeFilePath);
    }




    public String getFileId(){
       return this.activeFilePath;
    }

    private byte[] getSerializedEntry(BitCaskEntry entry) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(entry);
        return byteStream.toByteArray();
    }

    private void checkForPossibleNewActiveFile(int serializedLength) throws IOException {

        Path filePath = Paths.get(this.activeFilePath);
        long fileSizeInBytes = Files.size(filePath);

        int fileSize = (int) fileSizeInBytes;
        int total = fileSize + serializedLength;
        if(debug) System.out.println("size >>> "+total);

        if(total> MAX_SIZE){
           if(debug) System.out.println("File is too large to write");
            createNewActiveFile();
        }

    }
    public long writeToActiveFile(BitCaskEntry entry) throws IOException {
        if(debug){
            System.out.println("writeToActiveFile a entry of size = "+entry.getEntrySize());
            System.out.println("write at pos = "+this.nextFilePosition);
        }

        byte [] serializedEntry = getSerializedEntry(entry);
        if(debug) System.out.println("Serialized data " + serializedEntry.length);
        checkForPossibleNewActiveFile(serializedEntry.length);

        // Write the serialized entry to the file
        this.activeFile.seek(this.nextFilePosition);
        this.activeFile.write(serializedEntry);

        long filePosition = this.nextFilePosition; // get the filePosition
        this.nextFilePosition = this.activeFile.getFilePointer();    // Update nextFilePosition for the next write
        return filePosition;
    }

    public byte[] readFromFile(String filePath, long valuePosition , int valueSi) throws IOException, ClassNotFoundException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(valuePosition);


        ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(file.getFD()));
        BitCaskEntry entry = (BitCaskEntry) objectStream.readObject();
        if(entry == null) return null;

        if(debug)        System.out.println("Read From file : " + filePath +"at pos " + valuePosition); ;
        if(debug)         System.out.println(entry.toString());

        return entry.getValue();

    }



}

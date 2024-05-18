package Handlers;

import Converter.TypesConverter;
import Entries.BitCaskEntry;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ActiveFileHandler {
    private final int MAX_SIZE = 2000;
    private final String initialFilePath ="src/main/java/Storage/activeFile";
    private final String copy = ".copy";

    private FileHandler fileHandler;
    private RandomAccessFile activeFile;
//    private RandomAccessFile activeFileCopy;

    private String activeFilePath;
//    private String activeFilePathCopy;

    private long nextFilePosition;
    private int fileCounter;
    private TypesConverter converter;
    private boolean debug = true;

    public ActiveFileHandler(String activePath , int counter) throws IOException {
        this.converter = new TypesConverter();
        this.fileHandler = new FileHandler();

        if (activePath == null || activePath.isEmpty()) {
            this.activeFilePath = initialFilePath; // Default active file path
        } else {
            this.activeFilePath = activePath;
        }



        this.fileCounter = counter;
        initActiveFile(this.activeFilePath);
    }


    private void initActiveFile(String filePath) throws IOException {
        this.activeFile = new RandomAccessFile(filePath,"rw");
//        this.activeFileCopy = new RandomAccessFile(filePath+copy,"rw");

        this.nextFilePosition = this.activeFile.length();
    }


    private void createNewActiveFile() throws IOException {
        this.activeFile.close();
        this.fileHandler.copyFile(this.activeFilePath , this.activeFilePath+copy);

        this.activeFilePath = initialFilePath+String.valueOf(fileCounter++);

        initActiveFile(this.activeFilePath);

        if(debug) System.out.println("ADD NEW ACTIVE FILE "+ this.activeFilePath + " File write position = "+this.nextFilePosition);
    }


    private void checkForPossibleNewActiveFile(int addedEntryLength) throws IOException {
        Path filePath = Paths.get(this.activeFilePath);
        int totalSize = (int) (Files.size(filePath)) + addedEntryLength;

        if(totalSize> MAX_SIZE){
            if(debug) System.out.println("File is too large to write in");
            createNewActiveFile();
        }
    }


    public long writeNewEntryToActiveFile(BitCaskEntry entry) throws IOException {
        if(debug){
            System.out.println("writeToActiveFile a entry of size = "+entry.getEntrySize());
            System.out.println("write at pos = "+this.nextFilePosition);
        }

        checkForPossibleNewActiveFile(entry.getEntrySize());

        // write to active Path
        this.activeFile.seek(this.nextFilePosition);
        this.activeFile.write(entry.toByteArray());

//        // write to active replica
//        this.activeFileCopy.seek(this.nextFilePosition);
//        this.activeFileCopy.write(entry.toByteArray());

        long filePosition = this.nextFilePosition; // get the filePosition
        this.nextFilePosition = this.activeFile.getFilePointer();    // Update nextFilePosition for the next write
        return filePosition;
    }



    public byte[] getFromActiveFile(String filePath, long valuePosition , int valueSize) throws IOException {
        if(debug) System.out.println("readFromFile a file " + filePath + " of size = "+valueSize);
        byte [] value = new byte[valueSize];

        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(valuePosition);

        file.read(new byte[8] , 0 , 8);
        byte [] keySize = new byte[4];

        file.read(keySize , 0, 4);
        int size = this.converter.byteArrayToInt(keySize);

        file.seek(valuePosition + 16 + size);
        file.read(value , 0 , valueSize);
        return value;
    }



    public String getFileId(){
        return this.activeFilePath;
    }

    public RandomAccessFile getActiveFile(){
        return this.activeFile;
    }

}

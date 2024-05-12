package FilesHandler;

import Converter.TypesConverter;
import Entries.BitCaskEntry;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ActiveFileHandler {
    private final int MAX_SIZE = 500;
    private RandomAccessFile activeFile;
    private String activeFilePath;
    private long nextFilePosition;
    static private int fileCounter;
    private TypesConverter converter;
    private boolean debug = false;

    public ActiveFileHandler() throws IOException {
        this.converter = new TypesConverter();
        this.fileCounter = 0;
        this.activeFilePath = "src/Storage/activeFile";
        initActiveFile(this.activeFilePath);
    }


    private void initActiveFile(String filePath) throws IOException {
        this.activeFile = new RandomAccessFile(filePath,"rw");
        this.nextFilePosition = this.activeFile.length();
    }


    private void createNewActiveFile() throws IOException {
        this.activeFile.close();
        this.activeFilePath = activeFilePath+String.valueOf(fileCounter++);
        initActiveFile(this.activeFilePath);

        if(debug) System.out.println("ADD NEW ACTIVE FILE "+ this.activeFilePath);
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
        this.activeFile.seek(this.nextFilePosition);
        this.activeFile.write(entry.toByteArray());

        long filePosition = this.nextFilePosition; // get the filePosition
        this.nextFilePosition = this.activeFile.getFilePointer();    // Update nextFilePosition for the next write
        return filePosition;
    }

    public byte[] getFromActiveFile(String filePath, long valuePosition , int valueSize) throws IOException, ClassNotFoundException {
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


}

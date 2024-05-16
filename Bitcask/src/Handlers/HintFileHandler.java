package Handlers;

import Converter.TypesConverter;
import Entries.HintFileEntry;

import java.io.IOException;
import java.io.RandomAccessFile;

public class HintFileHandler {

    private  String currentFilePath="";
    private RandomAccessFile currentFile=null;
    private TypesConverter converter;

    public HintFileHandler() {
        this.converter = new TypesConverter();
    }

    public void writeToHintFile(String hintFilePath , HintFileEntry entry) throws IOException {
        if(hintFilePath != this.currentFilePath) {
            openNewHintFile(hintFilePath);
        }

        this.currentFile.seek(this.currentFile.length());
        this.currentFile.write(entry.toByteArray());
    }

    public HintFileEntry readHintFileEntryFromFile(RandomAccessFile file , long currentPosition) throws IOException {
        System.out.println("Read from hint file at position " + currentPosition);
        file.seek(currentPosition);

        byte [] timeStamp = new byte[8];
        byte [] keySize = new byte[4];
        byte [] valueSize = new byte[4];
        byte [] valuePositon = new byte[8];

        file.read(timeStamp , 0 , 8);
        file.read(keySize , 0 , 4);
        file.read(valueSize , 0 , 4);
        file.read(valuePositon , 0 , 8);

        long actualTimeStamp = this.converter.byteArrayToLong(timeStamp);
        int actualKeySize = this.converter.byteArrayToInt(keySize);
        int actualValueSize = this.converter.byteArrayToInt(valueSize);
        long actualValuePosition = this.converter.byteArrayToLong(valuePositon);

        byte [] key = new byte[actualKeySize];

        file.read(key , 0 , actualKeySize);

        return new HintFileEntry(actualTimeStamp, actualKeySize, actualValueSize, actualValuePosition, key);
    }

    private void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    private void openNewHintFile(String path) throws IOException {
        if(currentFile!=null)   this.currentFile.close();
        this.currentFile = new RandomAccessFile(path,"rw");
        setCurrentFilePath(path);
    }
}

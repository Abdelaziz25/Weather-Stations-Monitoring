package Handlers;

import Converter.TypesConverter;
import Entries.BitCaskEntry;


import java.io.IOException;
import java.io.RandomAccessFile;

public class BitCaskEntryHandler {

    private final TypesConverter converter;
    private long filePointer;

    public BitCaskEntryHandler(){
        this.converter = new TypesConverter();
        this.filePointer = 0;
    }

    public BitCaskEntry readBitCaskEntryFromFile(RandomAccessFile file ,long currentPosition) throws IOException {
//        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        //        setFilePointer(file.getFilePointer());
        file.seek(currentPosition);

        byte [] timeStamp = new byte[8];
        byte [] keySize = new byte[4];
        byte [] valueSize = new byte[4];

        file.read(timeStamp , 0 , 8);
        file.read(keySize , 0 , 4);
        file.read(valueSize , 0 , 4);


        long actualTimeStamp = this.converter.byteArrayToLong(timeStamp);
        int actualKeySize = this.converter.byteArrayToInt(keySize);
        int actualValueSize = this.converter.byteArrayToInt(valueSize);

        byte [] key = new byte[actualKeySize];
        byte [] value = new byte[actualValueSize];

        file.read(key , 0 , actualKeySize);
        file.read(value , 0 , actualValueSize);

        return new BitCaskEntry(actualTimeStamp , actualKeySize , actualValueSize , key , value);
    }

    public void setFilePointer(long filePointer) {
        this.filePointer = filePointer;
    }
    public long getFilePointer() {
        return this.filePointer;
    }


}


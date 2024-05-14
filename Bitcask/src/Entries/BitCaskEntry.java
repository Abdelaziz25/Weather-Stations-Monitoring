package Entries;

import Converter.TypesConverter;

import java.io.*;
import java.util.Arrays;

public class BitCaskEntry  implements Serializable {
    private long timeStamp;
    private int keyLength;
    private int valueLength ;
    private byte [] key;
    private byte [] value;

    private TypesConverter converter;

    public BitCaskEntry( long timeStamp ,  int keyLength, int valueLength, byte [] key, byte [] value){
        this.key = key;
        this.value = value;
        this.keyLength = keyLength;
        this.valueLength = valueLength;
        this.timeStamp = timeStamp;
        this.converter = new TypesConverter();
    }

    public int getEntrySize(){
        int size =0;
        size = Long.BYTES + Integer.BYTES + Integer.BYTES ;

        if (key != null)    size += key.length;
        if (value != null)  size += value.length;

        return size;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(this.converter.longToBytes(this.timeStamp));
        byteArrayOutputStream.write(this.converter.intToBytes(this.keyLength));
        byteArrayOutputStream.write(this.converter.intToBytes(this.valueLength));
        byteArrayOutputStream.write(this.key);
        byteArrayOutputStream.write(this.value);
        return byteArrayOutputStream.toByteArray();
    }




    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public int getValueLength() {
        return valueLength;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "BitCaskEntry{" +
                "timeStamp=" + timeStamp +
                ", keyLength=" + keyLength +
                ", valueLength=" + valueLength +
                ", key=" + Arrays.toString(key) +
                ", value=" + Arrays.toString(value) +
                '}';
    }

}

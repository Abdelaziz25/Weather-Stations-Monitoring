package Entries;

import Converter.TypesConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class HintFileEntry {
    private long tstamp;
    private int keySize ;
    private int valueSize ;
    private long valuePosition;
    private byte[] key;

    private TypesConverter converter;

    public HintFileEntry(long tstamp, int keySize, int valueSize, long valuePosition, byte[] key){
        this.tstamp = tstamp;
        this.keySize = keySize;
        this.valueSize = valueSize;
        this.valuePosition = valuePosition;
        this.key = key;
        this.converter = new TypesConverter();
    }

    public int getEntrySize(){
        int size = Long.BYTES + Integer.BYTES + Integer.BYTES + Long.BYTES;
        if (key != null)    size += key.length;
        return size;
    }


    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(this.converter.longToBytes(this.tstamp));
        byteArrayOutputStream.write(this.converter.intToBytes(this.keySize));
        byteArrayOutputStream.write(this.converter.intToBytes(this.valueSize));
        byteArrayOutputStream.write(this.converter.longToBytes(this.valuePosition));
        byteArrayOutputStream.write(this.key);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HintFileEntry{");
        sb.append("tstamp=").append(tstamp);
        sb.append(", keySize=").append(keySize);
        sb.append(", valueSize=").append(valueSize);
        sb.append(", valuePosition=").append(valuePosition);
        sb.append(", key=").append(Arrays.toString(key)); // Assuming key is a byte array representing a string
        sb.append('}');
        return sb.toString();
    }



    public long getTstamp() {
        return tstamp;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getValueSize() {
        return valueSize;
    }

    public byte[] getKey() {
        return key;
    }

    public long getValuePosition() {
        return valuePosition;
    }


}

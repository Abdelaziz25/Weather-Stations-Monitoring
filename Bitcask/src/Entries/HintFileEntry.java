package Entries;

import Converter.TypesConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(this.converter.longToBytes(this.tstamp));
        byteArrayOutputStream.write(this.converter.intToBytes(this.keySize));
        byteArrayOutputStream.write(this.converter.intToBytes(this.valueSize));
        byteArrayOutputStream.write(this.key);
        return byteArrayOutputStream.toByteArray();
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

    public void setTstamp(long tstamp) {
        this.tstamp = tstamp;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public void setValueSize(int valueSize) {
        this.valueSize = valueSize;
    }

    public void setValuePosition(long valuePosition) {
        this.valuePosition = valuePosition;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
}

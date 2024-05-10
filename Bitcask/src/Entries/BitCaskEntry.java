package Entries;

import java.io.Serializable;
import java.util.Arrays;

public class BitCaskEntry  implements Serializable {
    private long timeStamp;
    private int keyLength;
    private int valueLength ;
    private byte [] key;
    private byte [] value;


    public BitCaskEntry(byte [] key, byte [] value, int keyLength, int valueLength, long timeStamp){
        this.key = key;
        this.value = value;
        this.keyLength = keyLength;
        this.valueLength = valueLength;
        this.timeStamp = timeStamp;
    }

    public int getEntrySize(){
        int size =0;
        size = Long.BYTES + Integer.BYTES + Integer.BYTES ;

        if (key != null)    size += key.length;
        if (value != null)  size += value.length;

        return size;
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

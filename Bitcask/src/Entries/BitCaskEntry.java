package Entries;

import java.io.Serializable;

public class BitCaskEntry  implements Serializable {
    private byte [] value;
    private byte [] key;
    private int keyLength;
    private int valueLength ;
    private long timeStamp;

    public BitCaskEntry(byte [] key, byte [] value, int keyLength, int valueLength, long timeStamp){
        this.key = key;
        this.value = value;
        this.keyLength = keyLength;
        this.valueLength = valueLength;
        this.timeStamp = timeStamp;
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


}

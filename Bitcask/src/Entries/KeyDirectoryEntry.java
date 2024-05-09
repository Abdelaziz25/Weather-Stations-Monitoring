package Entries;

public class KeyDirectoryEntry {

    private long timeStamp;
    private int valueSize ;
    private long valuePosition;
    private String fileId ;

    public KeyDirectoryEntry(long timeStamp, int valueSize, long valuePosition, String fileId) {
        this.timeStamp = timeStamp;
        this.valueSize = valueSize;
        this.valuePosition = valuePosition;
        this.fileId = fileId;
    }
}

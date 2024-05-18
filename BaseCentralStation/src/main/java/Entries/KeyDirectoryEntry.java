package Entries;

public class KeyDirectoryEntry {

    private String fileId ;
    private long valuePosition;
    private int valueSize ;
    private long timeStamp;

    public KeyDirectoryEntry(long timeStamp, int valueSize, long valuePosition, String fileId) {
        this.timeStamp = timeStamp;
        this.valueSize = valueSize;
        this.valuePosition = valuePosition;
        this.fileId = fileId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getValueSize() {
        return valueSize;
    }

    public String getFileId() {
        return fileId;
    }

    public long getValuePosition() {
        return valuePosition;
    }

    @Override
    public String toString() {
        return "KeyDirectoryEntry{" +
                "fileId='" + fileId + '\'' +
                ", valuePosition=" + valuePosition +
                ", valueSize=" + valueSize +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

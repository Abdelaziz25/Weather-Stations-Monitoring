package DTO;

public class ActiveFileInfo {
    private String filePath;
    private int counter ;

    public ActiveFileInfo(String filePath, int counter) {
        this.filePath = filePath;
        this.counter = counter;
    }

    public ActiveFileInfo() {}

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
}

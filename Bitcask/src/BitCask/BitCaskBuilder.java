package BitCask;

import DTO.ActiveFileInfo;
import Entries.HintFileEntry;
import Entries.KeyDirectoryEntry;
import Handlers.ActiveFileHandler;
import Handlers.FileHandler;
import Handlers.HintFileHandler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BitCaskBuilder {

    boolean debug = false;
    private HintFileHandler hintFileHandler;
    private FileHandler fileHandler;

    public BitCaskBuilder(){
        this.hintFileHandler = new HintFileHandler();
        this.fileHandler = new FileHandler();
    }

    public ActiveFileInfo constructKeyDirectory(List<String> hintFilesPaths , HashMap<String , KeyDirectoryEntry> keyDir) throws IOException {
        String currentActiveHintFile = "";
        ActiveFileInfo activeFileInfo = new ActiveFileInfo("",-1);

        // read Hint File and append in hashMap
        for (String hintFile : hintFilesPaths) {
            keyDir = constructKeyDirectoryFromHintFile(hintFile, keyDir);

            ActiveFileInfo tempActiveFileInfo = this.fileHandler.getFilePathAndCounter(hintFile);

            if(debug) System.out.println("Current ActiveFileInfo: " + tempActiveFileInfo.getFilePath() +" Counter "+ tempActiveFileInfo.getCounter());

            if(tempActiveFileInfo.getCounter() > activeFileInfo.getCounter()) {
                activeFileInfo = tempActiveFileInfo;
            }
        }
        return activeFileInfo;

    }

    private HashMap<String, KeyDirectoryEntry> constructKeyDirectoryFromHintFile(String hintFile , HashMap<String , KeyDirectoryEntry> hashmap) throws IOException {
        if(debug) System.out.println("Constructing From File "+hintFile);

        RandomAccessFile file = new RandomAccessFile(hintFile, "r");
        long readingPosition =0;

        while(readingPosition < file.length()){
            HintFileEntry entry = hintFileHandler.readHintFileEntryFromFile(file, readingPosition);
            readingPosition+=entry.getEntrySize();


            if(debug) {
                System.out.println("Successfully reading hint entry " + entry.toString());
                System.out.println(" Next Reading Position "+readingPosition);
            }

            String key = Arrays.toString(entry.getKey());

            if(!hashmap.containsKey(key)){
                String fileId =  hintFile.substring(0, hintFile.lastIndexOf('.'));
                if(debug)   System.out.println("First time reading hint entry " + entry.toString() );
                hashmap.put(key , new KeyDirectoryEntry(entry.getTstamp(), entry.getValueSize(), entry.getValuePosition(), fileId));
            }else{
                KeyDirectoryEntry current = hashmap.get(key);

                if(current.getTimeStamp() < entry.getTstamp()){
                    String fileId =  hintFile.substring(0, hintFile.lastIndexOf('.'));
                    if(debug) System.out.println("updated version in this file>> " + entry.toString());
                    hashmap.put(key , new KeyDirectoryEntry(entry.getTstamp(), entry.getValueSize(), entry.getValuePosition(), fileId));
                }
            }
        }

        return hashmap;
    }
}

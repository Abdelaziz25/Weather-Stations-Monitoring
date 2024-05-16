package BitCask;

import Entries.HintFileEntry;
import Entries.KeyDirectoryEntry;
import Handlers.HintFileHandler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;

public class BitCaskBuilder {

    boolean debug = true;
    HintFileHandler hintFileHandler;

    public BitCaskBuilder(){
        this.hintFileHandler = new HintFileHandler();
    }

    public HashMap<String, KeyDirectoryEntry> constructKeyDirectoryFromHintFile(String hintFile , HashMap<String , KeyDirectoryEntry> hashmap) throws IOException {
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
                System.out.println("First time reading hint entry " + entry.toString() );
                hashmap.put(key , new KeyDirectoryEntry(entry.getTstamp(), entry.getValueSize(), entry.getValuePosition(), fileId));
            }else{
                KeyDirectoryEntry current = hashmap.get(key);

                if(current.getTimeStamp() < entry.getTstamp()){
                    String fileId =  hintFile.substring(0, hintFile.lastIndexOf('.'));
                    System.out.println("updated version in this file>> " + entry.toString());
                    hashmap.put(key , new KeyDirectoryEntry(entry.getTstamp(), entry.getValueSize(), entry.getValuePosition(), fileId));
                }
            }
        }

        return hashmap;
    }
}

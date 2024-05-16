package Controllers;

import Entries.BitCaskEntry;
import Entries.HintFileEntry;
import Entries.KeyDirectoryEntry;
import Handlers.BitCaskEntryHandler;
import Handlers.FileHandler;
import Visualization.ConsolePrinter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class CompactionController {


    private String mergePath = "src/Storage";
    private String hintExtension = ".hint";
    private int counter = 0;

    private final int MIN_COMPACTED_FILES = 4;

    private FileHandler fileHandler;
    private ConsolePrinter cp;
    private BitCaskEntryHandler bitCaskEntryHandler;
    private boolean debug = false;

    public CompactionController() {
        this.fileHandler = new FileHandler();
        this.bitCaskEntryHandler = new BitCaskEntryHandler();
        this.cp = new ConsolePrinter();
    }


    private List<BitCaskEntry> readAllEntries(String filePath) throws IOException {
        List<BitCaskEntry> bitCaskEntries = new ArrayList<>();

        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        long readingPosition =0;

        while(readingPosition<file.length()){
            BitCaskEntry entry = this.bitCaskEntryHandler.readBitCaskEntryFromFile(file , readingPosition);
            readingPosition += entry.getEntrySize();

            if(debug) {
                System.out.println("Successfully reading BitCask Entry " + entry.toString());
                System.out.println(" Next Reading Position "+readingPosition);
            }

            bitCaskEntries.add(entry);
        }

        return bitCaskEntries;
    }


    private HashMap<String, BitCaskEntry> findMostRecentEntries(List<String> filePaths) throws IOException{
        HashMap<String , BitCaskEntry> mostRecentEntries = new HashMap<>();

        for(String filePath : filePaths){
            List<BitCaskEntry> bitCaskEntries = readAllEntries(filePath);

            if(debug)       System.out.println("File in path >> " + filePath + " >>  has entries = " + bitCaskEntries.size());

            for(BitCaskEntry entry : bitCaskEntries){
                String key = Arrays.toString(entry.getKey());

                if(!mostRecentEntries.containsKey(key)){
                    mostRecentEntries.put(key , entry);
                }else{
                    BitCaskEntry current = mostRecentEntries.get(key);

                    if(entry.getTimeStamp() > current.getTimeStamp()){
                        if(debug)       System.out.println("Current TimeStamp " + current.getTimeStamp() + " Updated TimeStamp " + entry.getTimeStamp());
                        mostRecentEntries.put(key , entry);
                    }
                }
            }
        }
        return mostRecentEntries;
    }

    private HashMap<String , KeyDirectoryEntry> getUpdatedKeyDirectory(HashMap<String, BitCaskEntry> mostRecentEntries) throws IOException{

        String currentMergePath = this.mergePath + String.valueOf(this.counter);
        String currentHintPath = currentMergePath+this.hintExtension;

        RandomAccessFile mergedFile = new RandomAccessFile(currentMergePath, "rw");
        RandomAccessFile hintFile = new RandomAccessFile(currentHintPath, "rw");

        HashMap<String , KeyDirectoryEntry> updatedKeyDirectory = new HashMap<>();

        for (String key : mostRecentEntries.keySet()){
            BitCaskEntry entry = mostRecentEntries.get(key);

            long entryPos = mergedFile.getFilePointer();
            mergedFile.write(entry.toByteArray());

            updatedKeyDirectory.put(key , new KeyDirectoryEntry(entry.getTimeStamp() , entry.getValueLength(), entryPos , currentMergePath));

            HintFileEntry hintEntry = new HintFileEntry(entry.getTimeStamp() , entry.getKeyLength() , entry.getValueLength() , entryPos , entry.getKey() );
            hintFile.write(hintEntry.toByteArray());
        }
        this.counter++;
        mergedFile.close();
        hintFile.close();

        return updatedKeyDirectory;
    }


    public void compact(HashMap<String , KeyDirectoryEntry> originalKeyDirectory) throws IOException {
        List<String> filePaths = fileHandler.listFilesWithExtension("src/Storage",".copy");

        if(filePaths.size() < MIN_COMPACTED_FILES) return;

        if(debug){
            System.out.println("Compacting " + filePaths.size() + " files");
            for(String filePath : filePaths){
                System.out.println(filePath);
            }
        }


        HashMap<String, BitCaskEntry> mostRecentEntries = findMostRecentEntries(filePaths);

        if(debug)           this.cp.printCompactDictionary(mostRecentEntries);

        HashMap<String , KeyDirectoryEntry> updatedKeyDirectory = getUpdatedKeyDirectory(mostRecentEntries);

        if(debug){
            System.out.println("Updated KeyDirectory after compaction");
            this.cp.printKeyDirectory(updatedKeyDirectory);
        }

        for(String key : updatedKeyDirectory.keySet()){
            KeyDirectoryEntry currentEntry = originalKeyDirectory.get(key);
            KeyDirectoryEntry updatedEntry = updatedKeyDirectory.get(key);

            if(currentEntry.getTimeStamp()<=updatedEntry.getTimeStamp()){
                originalKeyDirectory.put(key , updatedEntry);
            }
        }

        if(debug) this.cp.printKeyDirectory(originalKeyDirectory);

    }


}

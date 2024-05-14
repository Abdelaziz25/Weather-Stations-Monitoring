package CompactController;

import Entries.BitCaskEntry;
import Entries.HintFileEntry;
import FilesHandler.FileHandler;
import Visualization.ConsolePrinter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//public class Compacter {
//
//    private File[] files;
//    private FileHandler fileHandler;
//    private ConsolePrinter cp;
//    private boolean debug = false;
//
//    public Compacter(File[] files) throws IOException {
//        this.files = files;
//        this.fileHandler = new FileHandler();
//        this.cp = new ConsolePrinter();
//    }
//
//    private HashMap<String, BitCaskEntry> getMostRecentEntriesFromFile() throws IOException, ClassNotFoundException {
//        HashMap<String , BitCaskEntry> mostRecentEntries = new HashMap<>();
//
//        for(File file : files){
//            String filePath = file.getPath();
//            List<BitCaskEntry> entries = fileHandler.readAllEntriesFromFile(filePath);
//
//            if(debug)       System.out.println("File in path >> " + filePath + " >>  has entries = " + entries.size());
//
//            for(BitCaskEntry entry : entries) {
//                if (!mostRecentEntries.containsKey(entry.getKey().toString())) {
//                    mostRecentEntries.put(entry.getKey().toString(), entry);
//                } else {
//
//                    BitCaskEntry existingEntry = mostRecentEntries.get(entry.getKey().toString());
//                    if (entry.getTimeStamp() > existingEntry.getTimeStamp()) {
//                        if(debug)       System.out.println("Enstance most updated than me >> ");
//                        mostRecentEntries.put(entry.getKey().toString(), entry); // Replace with more recent entry
//                    }
//                }
//            }
//        }
//
//        return mostRecentEntries;
//    }
//
//    public void startMerge() throws IOException, ClassNotFoundException {
//        HashMap<String, BitCaskEntry> mostRecentEntries = getMostRecentEntriesFromFile();
//
//        if(debug) this.cp.printCompactDictionary(mostRecentEntries);
//
//        Random random = new Random();
//        String mergedPath = "src/Storage/merged" + String.valueOf(random.nextInt(1000));
//        RandomAccessFile mergedFile = new RandomAccessFile(mergedPath, "rw");
//        String hintPath = mergedPath+"hint";
//        RandomAccessFile hintFile = new RandomAccessFile(hintPath, "rw");
//
//        for (String key : mostRecentEntries.keySet()){
//                BitCaskEntry entry = mostRecentEntries.get(key);
//                long entryPos = mergedFile.getFilePointer();
//                mergedFile.write(entry.toByteArray());
//
//                HintFileEntry hintEntry = new HintFileEntry(entry.getTimeStamp() , entry.getKeyLength() , entry.getValueLength() , entryPos , entry.getKey() );
//                hintFile.write(hintEntry.toByteArray());
//        }
//
////        deleteMergedFiles(files);
//
//        mergedFile.close();
//        hintFile.close();
//    }
//








//}

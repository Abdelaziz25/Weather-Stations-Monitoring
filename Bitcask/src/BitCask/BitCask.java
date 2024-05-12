package BitCask;

import CompactController.Compacter;
import Entries.BitCaskEntry;
import Entries.KeyDirectoryEntry;
import FilesHandler.ActiveFileHandler;
import FilesHandler.FileHelper;
//import FilesHandler.FileUtils;
import Visualization.ConsolePrinter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BitCask implements IBitCask {

    HashMap <String , KeyDirectoryEntry> keyDir;
    ActiveFileHandler activeFileHandler;
    ConsolePrinter cp ;
    Compacter compacter ;
    boolean debug = false;


    public BitCask() throws IOException {
        keyDir = new HashMap <> ();
        activeFileHandler = new ActiveFileHandler();
        cp = new ConsolePrinter();
    }


    public void put(byte[] key , byte[] value) throws IOException {
        long currentTimestamp = System.currentTimeMillis();
        BitCaskEntry newEntry = new BitCaskEntry(currentTimestamp , key.length , value.length , key , value);

        long valuePosition = activeFileHandler.writeNewEntryToActiveFile(newEntry);
        String fileId = activeFileHandler.getFileId();

        KeyDirectoryEntry keyDirEntry = new KeyDirectoryEntry(currentTimestamp , value.length , valuePosition , fileId);
        keyDir.put(key.toString(), keyDirEntry);

        if(debug){
            System.out.println(newEntry.toString());
            cp.printKeyDirectory(keyDir);
        }

    }


    public byte[] get(byte[] key) throws IOException, ClassNotFoundException {
        if(!keyDir.containsKey(key.toString())) {
            System.out.println("Key not found");
            return null;
        }

        KeyDirectoryEntry entry = keyDir.get(key.toString());
        byte [] value = activeFileHandler.getFromActiveFile(entry.getFileId() , entry.getValuePosition(), entry.getValueSize());

        if(value==null) return null;
        if(debug)   System.out.println("returned valu = "+ new String(value));
        return value;
    }

    public HashMap<String, KeyDirectoryEntry> getKeyDir() {
        return this.keyDir;
    }



    public void compact() throws IOException, ClassNotFoundException {

        String directoryPath = "src/Storage";

        // List all files in the directory
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("No files found in the specified directory.");
            return;
        }

        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }

        compacter = new Compacter(files);
        compacter.startMerge();
    }


}

package BitCask;

//import CompactController.Compacter;
import DTO.ActiveFileInfo;
import Entries.BitCaskEntry;
import Entries.HintFileEntry;
import Entries.KeyDirectoryEntry;
import FilesHandler.ActiveFileHandler;

import FilesHandler.FileHandler;
import FilesHandler.HintFileHandler;
import Visualization.ConsolePrinter;


import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class BitCask implements IBitCask {
    private HashMap <String , KeyDirectoryEntry> keyDir;
    private ActiveFileHandler activeFileHandler;

    private BitCaskBuilder bitCaskBuilder;

    private FileHandler fileHandler ;
    private HintFileHandler hintFileHandler;

    //    private Compacter compacter ;

    private ConsolePrinter cp ;
    private boolean debug = true;


    public BitCask() {
        bitCaskBuilder = new BitCaskBuilder();
        fileHandler = new FileHandler();
        hintFileHandler = new HintFileHandler();
        cp = new ConsolePrinter();
    }


    private void init() throws IOException {
        keyDir = new HashMap <> ();
        activeFileHandler = new ActiveFileHandler("",0);

        if(debug) System.out.println("Successfully Started BitCask");
    }




    public void start() throws IOException {
        List<String> hintFilesPaths = fileHandler.listFilesWithExtension("src/Storage", "hint");

        if (debug) {
            System.out.println("Number of Hint Files = " + hintFilesPaths.size());
            for (String file : hintFilesPaths) {
                System.out.println(file);
            }
        }

        if (hintFilesPaths.isEmpty()) {
            if (debug) System.out.println("No hint files found it's a new Start");
            init();
            return;
        }

        // constructKeyDir
        this.keyDir = new HashMap<>();
        String currentActiveHintFile = "";

        // read Hint File and append in hashMap
        for (String hintFile : hintFilesPaths) {
            this.keyDir = bitCaskBuilder.constructKeyDirectoryFromHintFile(hintFile, keyDir);

            if (debug) {
                System.out.println("Key Directory after reading hint file >> " + hintFile);
                cp.printKeyDirectory(this.keyDir);
            }

            if (hintFile.length() > currentActiveHintFile.length())
                currentActiveHintFile = hintFile;   // Get current Active File
        }

        ActiveFileInfo activeFileInfo = fileHandler.getFilePathAndCounter(currentActiveHintFile);
        if(debug)       System.out.println("Current Active File >> " + activeFileInfo.getFilePath());
        if(debug)       System.out.println("Counter >> " + activeFileInfo.getCounter());

        activeFileHandler = new ActiveFileHandler(activeFileInfo.getFilePath(), activeFileInfo.getCounter());
    }






    public void put(byte[] key , byte[] value) throws IOException {
        long currentTimestamp = System.currentTimeMillis();

        String KEY = Arrays.toString(key);

        // add to BitCask Storage
        BitCaskEntry newEntry = new BitCaskEntry(currentTimestamp , key.length , value.length , key , value);
        long valuePosition = activeFileHandler.writeNewEntryToActiveFile(newEntry);
        String fileId = activeFileHandler.getFileId();

        if(debug)       System.out.println("Successfully Added this Entry To BitCask File >> "+ newEntry.toString());

        // add To In-Memory Directory
        KeyDirectoryEntry keyDirEntry = new KeyDirectoryEntry(currentTimestamp , value.length , valuePosition , fileId);
        keyDir.put(KEY, keyDirEntry);

        if(debug)       System.out.println("Successfully Added this Entry To In Memory directory >> "+ keyDirEntry.toString());

        // add to Hint File
        HintFileEntry hintFileEntry = new HintFileEntry(currentTimestamp , key.length , value.length , valuePosition , key);
        hintFileHandler.writeToHintFile(fileId+".hint", hintFileEntry);

        if(debug)       System.out.println("Successfully Added this Entry To Hint File >> "+ hintFileEntry.toString());
    }


    public byte[] get(byte[] key) throws IOException, ClassNotFoundException {
        String KEY = Arrays.toString(key);

        if(!keyDir.containsKey(KEY) ){
            System.out.println("Key not found");
            return null;
        }

        KeyDirectoryEntry entry = keyDir.get(KEY);

        byte [] value = activeFileHandler.getFromActiveFile(entry.getFileId() , entry.getValuePosition(), entry.getValueSize());

        if(value==null) return null;
        if(debug)   System.out.println("Successfully Returned Value "+ Arrays.toString(value));
        return value;
    }

    public HashMap<String, KeyDirectoryEntry> getKeyDir() {
        return this.keyDir;
    }

    @Override
    public void close() {

    }

    //    public void compact() throws IOException, ClassNotFoundException {
//
//        String directoryPath = "src/Storage";
//
//        // List all files in the directory
//        File directory = new File(directoryPath);
//        File[] files = directory.listFiles();
//
//        if (files == null || files.length == 0) {
//            System.out.println("No files found in the specified directory.");
//            return;
//        }
//
//        for (File file : files) {
//            System.out.println(file.getAbsolutePath());
//        }
//
//        compacter = new Compacter(files);
//        compacter.startMerge();
//    }


}

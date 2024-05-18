package BitCask;

import Controllers.CompactionController;
import DTO.ActiveFileInfo;
import Entries.BitCaskEntry;
import Entries.HintFileEntry;
import Entries.KeyDirectoryEntry;
import Handlers.ActiveFileHandler;
import Handlers.BitCaskEntryHandler;
import Handlers.FileHandler;
import Handlers.HintFileHandler;
import Visualization.ConsolePrinter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class BitCask implements IBitCask {
    private HashMap <String , KeyDirectoryEntry> keyDir;
    private ActiveFileHandler activeFileHandler;

    private BitCaskBuilder bitCaskBuilder;

    private FileHandler fileHandler ;
    private HintFileHandler hintFileHandler;
    private BitCaskEntryHandler bitCaskEntryHandler;

    private CompactionController compactionController;

    private ConsolePrinter cp ;
    private boolean debug = true;
    private ScheduledExecutorService scheduler;

    public BitCask() {
        bitCaskBuilder = new BitCaskBuilder();
        fileHandler = new FileHandler();
        hintFileHandler = new HintFileHandler();
        bitCaskEntryHandler = new BitCaskEntryHandler();
        cp = new ConsolePrinter();
        compactionController = new CompactionController();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    private void init() throws IOException {
        keyDir = new HashMap <> ();
        activeFileHandler = new ActiveFileHandler("",0);

        if(debug) System.out.println("Successfully Started BitCask");
    }

    private void scheduleCompaction() {
        long initialDelay = 0;
        long period = 5; // Change this to the desired period (e.g., 1 for one hour)
        TimeUnit timeUnit = TimeUnit.MINUTES; // Change this to the desired time unit

        scheduler.scheduleAtFixedRate(() -> {
            try {
                compact();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, initialDelay, period, timeUnit);
    }

    private boolean checkForNewStart(boolean emptyHintFiles) throws IOException {
        if(emptyHintFiles){
                if (debug) System.out.println("No hint files found it's a new Start");
                init();
                return true;
        }
        return false;
    }

    @Override
    public void start() throws IOException {
        scheduleCompaction();
        List<String> hintFilesPaths = fileHandler.listFilesWithExtension("/mnt/Storage", "hint");

        if(checkForNewStart(hintFilesPaths.isEmpty()))      return;

        // constructKeyDir
        this.keyDir = new HashMap<>();
        ActiveFileInfo activeFileInfo = bitCaskBuilder.constructKeyDirectory(hintFilesPaths, this.keyDir);
        activeFileHandler = new ActiveFileHandler(activeFileInfo.getFilePath(), activeFileInfo.getCounter());

        if(debug){
            System.out.println("Number of Hint Files = " + hintFilesPaths.size());
            for (String file : hintFilesPaths)      System.out.println(file);
            System.out.println("KEY DIRECTORY AFTER CONSTRUCTION size = " + this.keyDir.size());
            this.cp.printKeyDirectory(this.keyDir);
            System.out.println("Current Active File >> " + activeFileInfo.getFilePath());
            System.out.println("Counter >> " + activeFileInfo.getCounter());
        }
    }


    @Override
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

    @Override
    public byte[] get(byte[] key) throws IOException, ClassNotFoundException {
        String KEY = Arrays.toString(key);

        if(!keyDir.containsKey(KEY)){
            System.out.println("Key not found");
            return null;
        }

        KeyDirectoryEntry entry = keyDir.get(KEY);
        byte [] value = bitCaskEntryHandler.getEntryInKeyDirectory(entry.getFileId(), entry.getValuePosition(), entry.getValueSize());
        if(value==null)          return null;
        if(debug)   System.out.println("Successfully Returned Value "+ Arrays.toString(value));
        return value;
    }

    public HashMap<String, KeyDirectoryEntry> getKeyDir() {
        return this.keyDir;
    }

    @Override
    public void close() throws IOException {
        this.activeFileHandler.getActiveFile().close();
        this.scheduler.shutdown();
    }

    @Override
    public void compact() throws IOException{
        if(debug)       System.out.println("TRY TO COMPACT");
        this.compactionController.compact(this.keyDir);
    }

}

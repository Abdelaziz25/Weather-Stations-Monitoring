import Entries.BitCaskEntry;
import Entries.KeyDirectoryEntry;
import FilesHandler.FileHelper;
import Visualization.ConsolePrinter;

import java.io.IOException;
import java.security.Timestamp;
import java.util.HashMap;

public class BitCask implements BitCaskInput{

    HashMap <byte[] , KeyDirectoryEntry> keyDir;
    FileHelper IactiveFile ;
    ConsolePrinter cp ;

    public BitCask() throws IOException {
        keyDir = new HashMap <> ();
        IactiveFile = new FileHelper();
        cp = new ConsolePrinter();
    }


    public void putKey(byte[] key , byte[] value) throws IOException {
        int keyLength = key.length;
        int valueLength = value.length;
        long tstamp = System.currentTimeMillis();

        BitCaskEntry newEntry = new BitCaskEntry(key , value , keyLength , valueLength , tstamp);
        long valuePosition = IactiveFile.writeToActiveFile(newEntry);
        String fileId = IactiveFile.getFileId();

        KeyDirectoryEntry keyDirEntry = new KeyDirectoryEntry(tstamp , valueLength , valuePosition , fileId);

        keyDir.put(key , keyDirEntry);
        cp.printKeyDirectory(keyDir);

    }
}

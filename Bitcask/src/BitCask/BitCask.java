package BitCask;

import Entries.BitCaskEntry;
import Entries.KeyDirectoryEntry;
import FilesHandler.FileHelper;
import Visualization.ConsolePrinter;

import java.io.IOException;
import java.util.HashMap;

public class BitCask implements IBitCask {

    HashMap <byte[] , KeyDirectoryEntry> keyDir;
    FileHelper IactiveFile ;
    ConsolePrinter cp ;

    public BitCask() throws IOException {
        keyDir = new HashMap <> ();
        IactiveFile = new FileHelper();
        cp = new ConsolePrinter();
    }


    public void put(byte[] key , byte[] value) throws IOException {
        int keyLength = key.length;
        int valueLength = value.length;
        long tstamp = System.currentTimeMillis();

        BitCaskEntry newEntry = new BitCaskEntry(key , value , keyLength , valueLength , tstamp);

        long valuePosition = IactiveFile.writeToActiveFile(newEntry);
        String fileId = IactiveFile.getFileId();

        KeyDirectoryEntry keyDirEntry = new KeyDirectoryEntry(tstamp , valueLength , valuePosition , fileId);

        keyDir.put(key , keyDirEntry);
        System.out.println(newEntry.toString());
//        cp.printKeyDirectory(keyDir);

    }


    public byte[] get(byte[] key) throws IOException, ClassNotFoundException {
        KeyDirectoryEntry entry = keyDir.get(key);

        if (entry == null) {
            System.out.println("Key not found");
            return null;
        }

        byte [] val = IactiveFile.readFromFile(entry.getFileId() , entry.getValuePosition(), entry.getValueSize());

        if(val==null) return null;
        System.out.println("returned valu = "+ new String(val));
        return val;

    }

    public HashMap<byte[], KeyDirectoryEntry> getKeyDir() {
        return this.keyDir;
    }
}

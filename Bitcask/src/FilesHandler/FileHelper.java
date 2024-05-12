package FilesHandler;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Converter.TypesConverter;
import Entries.BitCaskEntry;
import Entries.HintFileEntry;


public class FileHelper {




    private TypesConverter converter;

    private boolean debug = false;

    public FileHelper() throws IOException {

    }




    private byte[] getSerializedEntry(Object entry) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(entry);
        return byteStream.toByteArray();
    }



    public long writeEntryToFile(String filePath, Object entry) throws IOException {
        byte [] serializedEntry = getSerializedEntry(entry);
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        long valPos = file.length();
        file.seek(valPos);
        file.write(serializedEntry);
        return valPos;
    }



    public List<BitCaskEntry> readAllEntriesFromFile(String filePath) throws IOException, ClassNotFoundException {
        List<BitCaskEntry> entries = new ArrayList<>();
        RandomAccessFile file = new RandomAccessFile(filePath, "r");

        try {
            long currentPosition = 0;
            while (currentPosition < file.length()) {
                file.seek(currentPosition);
                ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(file.getFD()));
                BitCaskEntry entry = (BitCaskEntry) objectStream.readObject();
                if (entry != null) {
                    entries.add(entry);
                }
                currentPosition = file.getFilePointer(); // Update current position to the end of the last read entry
            }
        } finally {
            file.close();
        }

        return entries;
    }



}

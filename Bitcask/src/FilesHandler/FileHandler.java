package FilesHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Converter.TypesConverter;
import Entries.BitCaskEntry;


public class FileHandler {

    public FileHandler()  {}

    public static List<String> listFilesWithExtension(String directoryPath, String extension) {
        List<String> filePaths = new ArrayList<>();

        // Create a File object for the directory path
        File directory = new File(directoryPath);

        // Validate if the directory exists and is indeed a directory
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return filePaths; // Return empty list of file paths
        }

        File[] fileList = directory.listFiles(); // List all files in the directory

        if (fileList != null) {
            for (File file : fileList) { // Iterate through each file
                if (file.isFile()) {
                    if (file.getName().toLowerCase().endsWith("." + extension.toLowerCase())) {
                        // Add the absolute path of the file to the list
                        filePaths.add(file.getAbsolutePath());
                    }
                }
            }
        }

        return filePaths;
    }





////    private byte[] getSerializedEntry(Object entry) throws IOException {
////        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
////        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
////        objectStream.writeObject(entry);
////        return byteStream.toByteArray();
////    }
////
////
////
////    public long writeEntryToFile(String filePath, Object entry) throws IOException {
////        byte [] serializedEntry = getSerializedEntry(entry);
////        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
////        long valPos = file.length();
////        file.seek(valPos);
////        file.write(serializedEntry);
////        return valPos;
////    }
//
//
//
//    public List<BitCaskEntry> readAllEntriesFromFile(String filePath) throws IOException, ClassNotFoundException {
//        List<BitCaskEntry> entries = new ArrayList<>();
//        BitCaskEntryHandler bitCaskHandler = new BitCaskEntryHandler();
//
//        RandomAccessFile file = new RandomAccessFile(filePath, "r");
//        long fileLength = file.length();
//        long currentPtr = 0;
//
//        while (currentPtr < fileLength) {
//            BitCaskEntry entry = bitCaskHandler.readBitCaskEntryFromFile(filePath, currentPtr);
//            currentPtr = bitCaskHandler.getFilePointer();
//
//            System.out.println("Entry Readed from file  >>> "+ entry.toString());
//            entries.add(entry);
//        }
//
//        return entries;
//    }
//


}

package Visualization;

import Entries.KeyDirectoryEntry;

import java.util.HashMap;
import java.util.Map;

public class ConsolePrinter {

    public ConsolePrinter(){}

    public static void printKeyDirectory(HashMap<byte[], KeyDirectoryEntry> keyDir) {
        System.out.println("Key Directory Contents:");
        System.out.println("-----------------------");

        for (Map.Entry<byte[], KeyDirectoryEntry> entry : keyDir.entrySet()) {
            byte[] key = entry.getKey();
            KeyDirectoryEntry value = entry.getValue();

            // Convert byte array to a readable string
            String keyStr = byteArrayToHexString(key);

            // Print the key and corresponding KeyDirectoryEntry
            System.out.println("Key: " + keyStr);
            System.out.println("Value: " + value.toString()); // Assuming toString method is implemented in KeyDirectoryEntry
            System.out.println("-----------------------");
        }

        System.out.println("End of Key Directory Contents");
    }

    // Helper method to convert byte array to hexadecimal string
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}

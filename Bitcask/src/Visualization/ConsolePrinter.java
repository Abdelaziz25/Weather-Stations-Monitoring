package Visualization;

import Entries.KeyDirectoryEntry;

import java.util.HashMap;
import java.util.Map;

public class ConsolePrinter {

    public ConsolePrinter(){}

    public static void printKeyDirectory(HashMap<String, KeyDirectoryEntry> keyDir) {
        System.out.println("Key Directory Contents:");

        for (Map.Entry<String, KeyDirectoryEntry> entry : keyDir.entrySet()) {
            String key = entry.getKey();
            KeyDirectoryEntry value = entry.getValue();

            System.out.println("Key: " + key);
            System.out.println("Value: " + value.toString());
        }
        System.out.println("End of Key Directory Contents");
    }

}

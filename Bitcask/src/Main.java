import BitCask.BitCask;
import Visualization.ConsolePrinter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ConsolePrinter cp =  new ConsolePrinter();
        BitCask bitcask = new BitCask();

        // Example key and value byte arrays
        byte[] key1 = {0x12, 0x34, (byte) 0xAB, (byte) 0xCD}; // Byte array for key
        byte[] value1 = "Heo".getBytes(); // Byte array for value (string converted to bytes)

        byte[] key2 = {0x56, 0x78, (byte) 0xEF, (byte) 0x01}; // Another example key
        byte[] value2 = {0x41, 0x42, 0x43, 0x44, 0x45}; // Another example value (sequence of bytes)

        bitcask.put(key1 , value1);
        bitcask.put(key1 , value2);
        bitcask.put(key1, key2);
        bitcask.put(key1 , value1);
        bitcask.put(key1 , value2);
        bitcask.put(key1, key2);



        cp.printKeyDirectory(bitcask.getKeyDir());

        bitcask.get(key1);

//        bitcask.get(key1);
//        bitcask.get(key2);





    }
}
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

        byte[] key3 = {0x56, 0x78, (byte) 0xEF, (byte) 0x01};
        byte[] value3 = {0x41, 0x42, 0x43, 0x44, 0x45};

        byte[] key4 = {0x56, 0x78, (byte) 0xEF, (byte) 0x01};
        byte[] value4 = {0x41, 0x42, 0x43, 0x44, 0x45 , 0x51, 0x56, 0x78};


        bitcask.put(key1 , value4);
        bitcask.put(key2 , value3);
        bitcask.put(key3 , value2);
        bitcask.put(key4 , value1);


        System.out.println(bitcask.get(key1));
        bitcask.get("hhhhhhhhhhhhhhhh".getBytes());


        bitcask.put(key1 , value1);
        bitcask.put(key2, value2);
        bitcask.put(key3 , value3);
        bitcask.put(key4 , value4);


        bitcask.put(key1 , value1);
        bitcask.put(key1 , value2);
        bitcask.put(key1 , value3);


        System.out.println("Key Directory >>>>");
        cp.printKeyDirectory(bitcask.getKeyDir());
        System.out.println("-------------------------------------------------------------------------------------------------------------");


//        bitcask.compact();




//        cp.printKeyDirectory(bitcask.getKeyDir());
//
//        bitcask.get(key1);

//        bitcask.get(key1);
//        bitcask.get(key2);

    }
}
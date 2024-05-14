import BitCask.BitCask;
import Visualization.ConsolePrinter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ConsolePrinter cp =  new ConsolePrinter();
        BitCask bitcask = new BitCask();

        bitcask.start();

        byte[] key1 = "key1".getBytes();
        byte[] value1 = "val1".getBytes();

        byte[] key2 = "key2".getBytes();
        byte[] value2 = "val2".getBytes();

        byte[] key3 = "key3".getBytes();
        byte[] value3 = "val3".getBytes();

        byte[] key4 = "key4".getBytes();
        byte[] value4 = "val4".getBytes();


//        bitcask.put(key1 , value1);
//        bitcask.put(key2 , value2);
//        bitcask.put(key3 , value3);
//        bitcask.put(key4 , value4);

        cp.printKeyDirectory(bitcask.getKeyDir());

        bitcask.get(key1);
        bitcask.get(key2);
        bitcask.get(key3);
        bitcask.get(key4);
        bitcask.get("mfesssh".getBytes());



//        bitcask.start();

//        bitcask.get(key1);
//        bitcask.get("laaaaaaaaa".getBytes());


//        System.out.println(bitcask.get(key1));
//        bitcask.get("hhhhhhhhhhhhhhhh".getBytes());
//
//
//        bitcask.put(key1 , value1);
//        bitcask.put(key2, value2);
//        bitcask.put(key3 , value3);
//        bitcask.put(key4 , value4);


//        bitcask.put(key1 , value1);
//        bitcask.put(key1 , value2);
//        bitcask.put(key1 , value3);
//
//
//        System.out.println("Key Directory >>>>");
//        cp.printKeyDirectory(bitcask.getKeyDir());
//        System.out.println("-------------------------------------------------------------------------------------------------------------");
//
//
////        bitcask.compact();
////        cp.printKeyDirectory(bitcask.getKeyDir());
////
////        bitcask.get(key1);
//
////        bitcask.get(key1);
////        bitcask.get(key2);

    }
}
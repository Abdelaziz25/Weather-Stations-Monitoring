
import BitCask.BitCask;
import java.io.IOException;


class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        byte[] key1 = "key1".getBytes();
        byte[] value1 = "val1dgawuiwuory".getBytes();

        byte[] key2 = "key2".getBytes();
        byte[] value2 = "val2yreeeeeeeeeeeeeeewuiytiw".getBytes();

        byte[] key3 = "key3".getBytes();
        byte[] value3 = "val3BBBBBBBBBBBBBBBBBBBBBBBB".getBytes();

        byte[] key4 = "key4".getBytes();
        byte[] value4 = "val4VGRYRYRTUTITY".getBytes();

        byte[] key5 = "key5".getBytes();
        byte[] value5 = "val5VVVVVVVVVVVVVV22".getBytes();

        byte[] key6 = "key6".getBytes();
        byte[] value6 = "val658329607Q3063".getBytes();

        byte[] key7 = "key7".getBytes();
        byte[] value7 = "val70685T5342122".getBytes();

        byte[] key8 = "key8".getBytes();
        byte[] value8 = "val821476125026".getBytes();

        byte[] key9 = "key9".getBytes();
        byte[] value9 = "val92AGJHGSGIS".getBytes();

        byte[] key10 = "key10".getBytes();
        byte[] value10 = "val10VVVVVVVVVVVV22".getBytes();


        BitCask bitcask = new BitCask();
        bitcask.start();

//        bitcask.put(key1 , value1);
//        bitcask.put(key2 , value2);
//        bitcask.put(key3 , value3);
//        bitcask.put(key4 , value4);
//        bitcask.put(key5 , value5);
//        bitcask.put(key6 , value6);
//        bitcask.put(key7 , value7);
//        bitcask.put(key8 , value8);
//
//        bitcask.put(key9 , value9);
//        bitcask.put(key10 , value10);
//        bitcask.put(key1 , value1);
//        bitcask.put(key2 , value1);
//        bitcask.put(key3 , value1);
//        bitcask.put(key4 , value1);
//        bitcask.put(key5 , value1);

        bitcask.get(key1);
        bitcask.get(key10);
        bitcask.get(key8);
        bitcask.get(key9);
        bitcask.get("NOOOOO".getBytes());

//        bitcask.close();
    }
}
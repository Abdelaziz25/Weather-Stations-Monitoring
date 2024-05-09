import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        BitCask bitcask = new BitCask();

        // Example key and value byte arrays
        byte[] key1 = {0x12, 0x34, (byte) 0xAB, (byte) 0xCD}; // Byte array for key
        byte[] value1 = "Hello".getBytes(); // Byte array for value (string converted to bytes)

        byte[] key2 = {0x56, 0x78, (byte) 0xEF, (byte) 0x01}; // Another example key
        byte[] value2 = {0x41, 0x42, 0x43, 0x44, 0x45}; // Another example value (sequence of bytes)

        bitcask.putKey(key1 , value1);
        bitcask.putKey(key2 , value2);



    }
}
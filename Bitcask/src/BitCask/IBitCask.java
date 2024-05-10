package BitCask;

import java.io.IOException;

public interface IBitCask {

    public void put(byte[] key , byte[] value) throws IOException;

    public byte [] get(byte[] key) throws IOException, ClassNotFoundException;


}

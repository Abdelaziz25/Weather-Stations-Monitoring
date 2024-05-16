package BitCask;

import java.io.IOException;

public interface IBitCask {

    public void start() throws IOException ;

    public void put(byte[] key , byte[] value) throws IOException;

    public byte [] get(byte[] key) throws IOException, ClassNotFoundException;

   public void close() throws IOException;

   public void compact() throws IOException;
}

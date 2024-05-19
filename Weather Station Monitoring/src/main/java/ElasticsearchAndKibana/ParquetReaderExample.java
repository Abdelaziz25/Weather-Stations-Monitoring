package ElasticsearchAndKibana;

import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetReader.Builder;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.schema.MessageType;
import java.io.IOException;

public class ParquetReaderExample {
    public static void readParquetFile(String parquetFilePath) {
        try {
            // Define Parquet file reader
            Builder<Group> reader = ParquetReader.builder(new GroupReadSupport(), new org.apache.hadoop.fs.Path(parquetFilePath));
            ParquetReader<Group> parquetReader = reader.build();

            // Read Parquet file
            Group group;
            while ((group = parquetReader.read()) != null) {
                // Process each Parquet record
                // Example: print the record
                System.out.println(group);
            }

            // Close the Parquet reader
            parquetReader.close();
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
    }
    
}

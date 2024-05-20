package ElasticsearchAndKibana;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetReader.Builder;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
public class ParquetReaderExample {
    public static void readParquetFile(String parquetFilePath) {
        try {
            // Define Parquet file reader
            Builder<Group> reader = ParquetReader.builder(new GroupReadSupport(), new org.apache.hadoop.fs.Path(parquetFilePath));
            ParquetReader<Group> parquetReader = reader.build();
            // Read Parquet file
            Group group;
            RestClient restClient = RestClient.builder(new HttpHost("elasticsearch-service", 9200, "http")).build();
            Response response = null;
            ObjectMapper objectMapper = new ObjectMapper();
            while ((group = parquetReader.read()) != null) {
                // Process each Parquet record
                // Example: print the record
                Map<String, Object> groupMap = groupToMap(group);
                String jsonString = objectMapper.writeValueAsString(groupMap);
                System.out.println(jsonString);
//              Create an IndexRequest
                int stationId = group.getInteger("station_id", 0);
                try {
                    HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
                    Request request = new Request("POST", "/data/_doc");
                    request.setEntity(entity);
                    response = restClient.performRequest(request);
                    System.out.println(response);
                } catch (Exception e) {
                    // Handle the exception appropriately, such as logging the error
                    System.err.println("Failed to index document for station ID: " + stationId);
                    e.printStackTrace();
                }
                System.out.println(group);
            }
            // Close the Parquet reader
            parquetReader.close();
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
    }
    private static Map<String, Object> groupToMap(Group group) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < group.getType().getFieldCount(); i++) {
            String fieldName = group.getType().getFieldName(i);
            if (group.getType().getType(i).isPrimitive()) {
                if ("station_id".equals(fieldName) || "s_no".equals(fieldName)) {
                    map.put(fieldName, group.getInteger(i, 0)); // Convert to integer
                } else {
                    map.put(fieldName, group.getValueToString(i, 0));
                }
            } else {
                Map<String, Object> nestedMap = new HashMap<>();
                for (int j = 0; j < group.getFieldRepetitionCount(i); j++) {
                    Group nestedGroup = group.getGroup(i, j);
                    for (int k = 0; k < nestedGroup.getType().getFieldCount(); k++) {
                        String nestedFieldName = nestedGroup.getType().getFieldName(k);
                        nestedMap.put(nestedFieldName, nestedGroup.getValueToString(k, 0));
                    }
                }
                map.put(fieldName, nestedMap);
            }
        }
        return map;
    }

}

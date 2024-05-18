package BaseCentralStation;
import BitCask.BitCask;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.OutputFile;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WeatherDataConsumer {
    private static final int BATCH_SIZE = 100;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static String projectDir = System.getProperty("user.dir");
    public static int fileIndex =0;
    public static void main(String[] args) throws IOException {
        String avroSchemaFilePath = projectDir + "/src/main/java/BaseCentralStation/weather_record.avsc";
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-release.kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "weather-data-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("Project"));
        Schema avroSchema = new Schema.Parser().parse(new File(avroSchemaFilePath));
        int recordCount = 0;
        Map<Integer, List<GenericRecord>> stationRecordsMap = new HashMap<>();
            for(int i=1;i<11;i++)
            {
                stationRecordsMap.put(i, new ArrayList<>());
            }


        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    GenericRecord avroRecord = createAvroRecord(record.value(), avroSchema);
                    recordCount++;
                    System.out.println(record.value());
                    int stationId = (int) avroRecord.get("station_id");
                    List<GenericRecord> stationRecords = stationRecordsMap.get(stationId);
                    stationRecords.add(avroRecord);
                    if (recordCount >= BATCH_SIZE) {
                        // Write the batch to a Parquet file
                        try {
                            writeRecordsToParquet(stationId, stationRecords, avroSchema);
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Optionally handle the exception, e.g., skip the batch
                        } finally {
                            stationRecords.clear();
                            recordCount = 0;
                        }
                    }
                    BitCask bitcask = new BitCask();
                    bitcask.start();
                    byte[] byteArrayKey = record.value().getBytes();
                    byte[] byteArrayValue = record.value().getBytes();
                    bitcask.put(byteArrayKey , byteArrayValue);
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static GenericRecord createAvroRecord(String jsonString, Schema schema) throws IOException {
        JSONObject json = new JSONObject(jsonString);
        GenericRecordBuilder recordBuilder = new GenericRecordBuilder(schema);
        recordBuilder.set("station_id", json.getInt("station_id"));
        recordBuilder.set("s_no", json.getInt("s_no"));
        recordBuilder.set("status_timestamp", json.getLong("status_timestamp"));
        recordBuilder.set("battery_status", json.getString("battery_status"));
        JSONObject weatherJson = json.getJSONObject("weather");
        GenericRecord weatherRecord = new GenericData.Record(schema.getField("weather").schema());
        weatherRecord.put("temperature", weatherJson.getInt("temperature"));
        weatherRecord.put("humidity", weatherJson.getInt("humidity"));
        weatherRecord.put("wind_speed", weatherJson.getInt("wind_speed"));
        recordBuilder.set("weather", weatherRecord);
        return recordBuilder.build();
    }
    private static void writeRecordsToParquet(int stationId, List<GenericRecord> records, Schema schema) throws IOException {
        LocalDate today = LocalDate.now();
        String dateDirName = today.format(DATE_FORMATTER);
        String stationDirPath = "/mnt/Volume" + "/station" + stationId +"/"+dateDirName ;
        String parquetFilePath = stationDirPath+ "/Project_"+ System.currentTimeMillis()+ ".parquet";;
        Path path = new Path(parquetFilePath);
        ParquetWriter<GenericRecord> parquetWriter = null;
        parquetWriter = AvroParquetWriter.<GenericRecord>builder(path).withSchema(schema).build();
            for (GenericRecord record : records) {
                parquetWriter.write(record);
            }
            if (parquetWriter != null) {
                parquetWriter.close(); // Close Parquet writer outside the loop
            }
            fileIndex++;
    }
}

package DataProcessing;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class WeatherDataConsumer {
    private static final int BATCH_SIZE = 100;

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "weather-data-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("Lab4"));

        Schema avroSchema = new Schema.Parser().parse(new File("C:\\Users\\abdel\\Desktop\\Connect-4\\Weather-Stations-Monitoring\\BaseCentralStation\\src\\main\\java\\DataProcessing\\weather_record.avsc"));
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "file:///");

        List<GenericRecord> recordsBatch = new ArrayList<>();
        int recordCount = 0;
        int fileIndex = 0;
        HadoopOutputFile outputFile = null;
        ParquetWriter<GenericRecord> parquetWriter = null;

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    GenericRecord avroRecord = createAvroRecord(record.value(), avroSchema);
                    recordsBatch.add(avroRecord);
                    recordCount++;
                    System.out.println(recordCount);
                    if (recordCount >= BATCH_SIZE) {
                        // Write the batch to a Parquet file
                        if (outputFile == null) {
                            outputFile = HadoopOutputFile.fromPath(new Path("C:\\Users\\abdel\\Desktop\\Project" + fileIndex + ".parquet"), conf);
                            fileIndex++;
                            parquetWriter = AvroParquetWriter.<GenericRecord>builder(outputFile)
                                    .withSchema(avroSchema)
                                    .withCompressionCodec(CompressionCodecName.SNAPPY)
                                    .build();
                        }
                        for (GenericRecord batchRecord : recordsBatch) {
                            parquetWriter.write(batchRecord);
                        }
                        recordsBatch.clear();
                        recordCount = 0;
                        if (parquetWriter != null) {
                            parquetWriter.close(); // Close Parquet writer
                            parquetWriter = null;
                        }
                        outputFile = null;
                    }
                }
            }
        } finally {
            consumer.close();
            if (parquetWriter != null) {
                parquetWriter.close();
            }
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
}

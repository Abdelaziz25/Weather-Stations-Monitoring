package DataAcquisition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // Set up Kafka producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


//        String latitudeStr = System.getenv("latitude");
//        String longitudeStr = System.getenv("longitude");
//        String stationIdStr = System.getenv("StationId");
//
//        // Convert latitude, longitude, and StationId to appropriate types
//        double latitude = Double.parseDouble(latitudeStr);
//        double longitude = Double.parseDouble(longitudeStr);
//        int stationId = Integer.parseInt(stationIdStr);

        // Create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Instantiate OpenMeteoAPI with the Kafka producer
        OpenMeteoAPI weatherAdapter = new OpenMeteoAPI(producer);


        // Geo coordinates
//        double latitude = 31.2018;
//        double longitude = 29.9158;

        // Fetch and publish weather data
        weatherAdapter.fetchAndPublishWeatherData(1,31.2018, 29.9158);

        // Close the Kafka producer
        producer.close();
    }
}

package DataAcquisition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // Set up Kafka producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Instantiate OpenMeteoAPI with the Kafka producer
        OpenMeteoAPI weatherAdapter = new OpenMeteoAPI(producer);


        // Geo coordinates
        double latitude = 31.2018;
        double longitude = 29.9158;

        // Fetch and publish weather data
        weatherAdapter.fetchAndPublishWeatherData(latitude, longitude);

        // Close the Kafka producer
        producer.close();
    }
}


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.json.JSONObject;

public class RainTriggerDetectionApp {
    private static KafkaProducer<String, String> producer;
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "rain-trigger-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-release.kafka:9092");

        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        // Add Kafka Streams processing topology
        KStream<String, String> weatherStream = builder.stream("Project", Consumed.with(Serdes.String(), Serdes.String()));

        weatherStream.filter((key, value) -> isRaining(value))
                .to("rain-triggers", Produced.with(Serdes.String(), Serdes.String()));

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-release.kafka:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);
        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, config);
        streams.start();
    }

    private static boolean isRaining(String value) {
        try {
            // Parse the JSON message
            JSONObject json = new JSONObject(value);

            // Extract humidity from the weather data
            JSONObject weatherData = json.getJSONObject("weather");
            int humidity = weatherData.getInt("humidity");

            // Check if humidity is higher than 70%
            if (humidity > 70) {
                // Print value if it's raining
                System.out.println("Raining detected: " + value);
                String message = "Raining detected: " + value;
                producer.send(new ProducerRecord<>("processor", message));
                return true;
            }
        } catch (Exception e) {
            // Handle parsing or any other exceptions
            e.printStackTrace();
        }
        return false;
    }
}

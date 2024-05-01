package WeatherStation;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaProducerExample {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Set up a loop to send messages to Kafka
        String topic = "my_first";
        try {
            while (true) {
                // Generate weather status message using WeatherStationMock
                String message = WeatherStationMock.generateWeatherStatusMessage();
                System.out.println(message);
                // Send the message to Kafka
                producer.send(new ProducerRecord<>(topic, message), (metadata, exception) -> {
                    if (exception != null) {
                        exception.printStackTrace();
                    } else {
                        System.out.println("Message sent successfully. Offset: " + metadata.offset());
                    }
                });

                // Sleep for 1 second
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}

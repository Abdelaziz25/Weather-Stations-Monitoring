package WeatherStation;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaWeatherProducer {
    private KafkaProducer<String, String> producer;
    private String topic;

    public KafkaWeatherProducer(String bootstrapServers, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    public void sendWeatherData(String weatherData) {
        producer.send(new ProducerRecord<>(topic, weatherData), (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Message sent successfully. Offset: " + metadata.offset());
            }
        });
    }

    public void close() {
        producer.close();
    }
}

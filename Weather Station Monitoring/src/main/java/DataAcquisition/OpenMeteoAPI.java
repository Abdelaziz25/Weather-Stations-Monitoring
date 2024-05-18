package DataAcquisition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class OpenMeteoAPI {
    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/weather";

    private final KafkaProducer<String, String> kafkaProducer;
    private final Random random = new Random();
    public OpenMeteoAPI(KafkaProducer<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void fetchAndPublishWeatherData(double latitude, double longitude) {
        try {
            // Construct API URL with latitude, longitude, and other parameters
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" +
                    latitude + "&longitude=" + longitude +
                    "&hourly=relativehumidity_2m,windspeed_80m,temperature_80m&" +
                    "current_weather=true&temperature_unit=fahrenheit&timeformat=unixtime" +
                    "&forecast_days=1&timezone=Africa%2FCairo";

            // Create HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(response.toString());
                    System.out.println(jsonObject);
                 // Extract hourly data object
                    JSONObject hourlyData = jsonObject.getJSONObject("hourly");

                 // Extract arrays for each parameter
                    JSONArray relativeHumidityArray = hourlyData.getJSONArray("relativehumidity_2m");
                    JSONArray windSpeedArray = hourlyData.getJSONArray("windspeed_80m");
                    JSONArray temperatureArray = hourlyData.getJSONArray("temperature_80m");
                    JSONArray timeArray = hourlyData.getJSONArray("time");

                 // Iterate through the arrays
                    for (int i = 0; i < relativeHumidityArray.length(); i++) {
                        int relativeHumidity = relativeHumidityArray.getInt(i);
                        int windSpeed = windSpeedArray.getInt(i);
                        int temperature = temperatureArray.getInt(i);
                        long time = timeArray.getLong(i); // Assuming time is in Unix timestamp format

                        // Publish message to Kafka
                        if (!(random.nextDouble() <= 0.1)) {
                            JSONObject kafkaMessage = WeatherMessageBuilder.buildWeatherMessage(1, i+1, relativeHumidity, windSpeed, temperature, time);
                            kafkaProducer.send(new ProducerRecord<>("Lab4", "1",kafkaMessage.toString()));
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.getCause();
                        }
                    }
                }
            } else {
                System.err.println("Failed to fetch weather data from Open-Meteo API. Response code: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Failed to fetch weather data: " + e.getMessage());
        }
    }

}

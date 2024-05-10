package DataAcquisition;

import org.json.JSONObject;

import java.util.Random;

public class WeatherMessageBuilder {

    // Method to build weather message with specified attributes
    public static JSONObject buildWeatherMessage(int stationId, int sNo, int relativeHumidity, int windSpeed, int temperature, long time) {
        JSONObject kafkaMessage = new JSONObject();
        kafkaMessage.put("station_id", stationId);
        kafkaMessage.put("s_no", sNo);
        kafkaMessage.put("battery_status", getRandomBatteryStatus());
        kafkaMessage.put("status_timestamp", time);
        JSONObject weatherData = new JSONObject();
        weatherData.put("humidity", relativeHumidity);
        weatherData.put("temperature", temperature);
        weatherData.put("wind_speed", windSpeed);
        kafkaMessage.put("weather", weatherData);
        return kafkaMessage;
    }

    // Method to randomly select battery status
    private static String getRandomBatteryStatus() {
        Random random = new Random();
        double probability = random.nextDouble();
        if (probability < 0.4) {
            return "Medium"; // Medium battery
        } else if (probability < 0.7) {
            return "Low"; // Low battery
        } else {
            return "High"; // High battery
        }
    }
}

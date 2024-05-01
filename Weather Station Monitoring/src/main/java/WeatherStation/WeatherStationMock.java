package WeatherStation;

import java.util.Random;

public class WeatherStationMock {
    private static final String[] BATTERY_STATUSES = {"low", "medium", "high"};

    public static String generateWeatherStatusMessage() {
        Random random = new Random();
        long stationId = 1;

        // Generate random battery status
        String batteryStatus = getRandomBatteryStatus(random);

        // Generate weather status message
        long timestamp = System.currentTimeMillis() / 1000; // Unix timestamp in seconds
        int humidity = random.nextInt(101); // 0 to 100
        int temperature = random.nextInt(101); // 0 to 100 Fahrenheit
        int windSpeed = random.nextInt(101); // 0 to 100 km/h

        // Construct the JSON message
        String message = String.format("{\n" +
                        "  \"station_id\": %d,\n" +
                        "  \"battery_status\": \"%s\",\n" +
                        "  \"status_timestamp\": %d,\n" +
                        "  \"weather\": {\n" +
                        "    \"humidity\": %d,\n" +
                        "    \"temperature\": %d,\n" +
                        "    \"wind_speed\": %d\n" +
                        "  }\n" +
                        "}\n",
                stationId, batteryStatus, timestamp,
                humidity, temperature, windSpeed);

        return message;
    }

    private static String getRandomBatteryStatus(Random random) {
        double probability = random.nextDouble();
        if (probability < 0.3) {
            return BATTERY_STATUSES[0]; // Low battery
        } else if (probability < 0.7) {
            return BATTERY_STATUSES[1]; // Medium battery
        } else {
            return BATTERY_STATUSES[2]; // High battery
        }
    }
}

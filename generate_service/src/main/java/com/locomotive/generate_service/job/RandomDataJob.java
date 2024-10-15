package com.locomotive.generate_service.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locomotive.generate_service.service.KafkaService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class RandomDataJob implements Job {

    @Autowired
    private KafkaService kafkaService;

    private final Random random = new Random();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String locoCode = "KRT_" + String.format("%03d", random.nextInt(7) + 1);  // Generate a random locomotive ID
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z"; // Timestamp in ISO format

        // Randomly generated sensor data
        double speed = formatDouble(random.nextDouble() * 100); // Speed
        double engineTemperature = formatDouble(random.nextDouble() * 150); // Temperature (up to 150 for testing)
        double brakePressure = formatDouble(random.nextDouble() * 10); // Brake pressure (up to 10 bar)
        double latitude = formatDouble(-6.0 + random.nextDouble() * 1.0); // Latitude
        double longitude = formatDouble(106.0 + random.nextDouble() * 1.0); // Longitude
        double elevation = (int) Math.round(random.nextDouble() * 100); // Elevation
        double vibrationAmplitude = formatDouble(random.nextDouble() * 3); // Vibration amplitude
        int vibrationFrequency = (int) Math.round(random.nextDouble() * 100); // Vibration frequency

        // Determine maintenance warnings based on sensor data
        boolean overheatWarning = engineTemperature > 100; // Overheat if temperature exceeds 100°C
        boolean brakePressureWarning = brakePressure < 3 || brakePressure > 6; // Warning if outside of 3-6 bar
        boolean vibrationWarning = vibrationAmplitude > 2; // Warning if amplitude exceeds 2 G-force

        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("speed", Map.of("value", speed, "unit", "km/h"));
        sensorData.put("engineTemperature", Map.of("value", engineTemperature, "unit", "C"));
        sensorData.put("brakePressure", Map.of("value", brakePressure, "unit", "bar"));
        sensorData.put("gps", Map.of("latitude", latitude,
                "longitude", longitude,
                "elevation", elevation));
        sensorData.put("vibration", Map.of("amplitudo", vibrationAmplitude,
                "frekuensi", vibrationFrequency,
                "unit", "G-force"));
        sensorData.put("doorStatus", Map.of("value", random.nextBoolean() ? "close" : "open",
                "timestamp", timestamp));

        // Creating the full data structure
        Map<String, Object> data = new HashMap<>();
        data.put("locomotiveCode", locoCode);
        data.put("timestamp", timestamp);
        data.put("sensorData", sensorData);
        data.put("maintenanceWarning", Map.of("overheatWarning", overheatWarning,
                "brakePressureWarning", brakePressureWarning,
                "vibrationWarning", vibrationWarning));

        System.out.println("Generated random data by Quartz: " + data);

        // Convert the data map to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = null;
        try {
            jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaService.sendMessage(jsonData);
    }

    private double formatDouble(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

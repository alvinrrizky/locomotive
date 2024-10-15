package com.locomotive.consume_service.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sensor-data")
public class LocomotiveSensor {
    @Id
    private String id;
    private String locomotiveCode;
    private LocalDateTime timestamp;
    private SensorData sensorData;
    private MaintenanceWarning maintenanceWarning;

    @Data
    public static class SensorData {
        private Speed speed;
        private EngineTemperature engineTemperature;
        private BrakePressure brakePressure;
        private GPS gps;
        private Vibration vibration;
        private DoorStatus doorStatus;
    }

    @Data
    public static class Speed {
        private double value;
        private String unit;
    }

    @Data
    public static class EngineTemperature {
        private double value;
        private String unit;
    }

    @Data
    public static class BrakePressure {
        private double value;
        private String unit;
    }

    @Data
    public static class GPS {
        private double latitude;
        private double longitude;
        private int elevation;
    }

    @Data
    public static class Vibration {
        private double amplitudo;
        private int frekuensi;
        private String unit;
    }

    @Data
    public static class DoorStatus {
        private String value;
        private LocalDateTime timestamp;
    }

    @Data
    public static class MaintenanceWarning {
        private boolean overheatWarning;
        private boolean brakePressureWarning;
        private boolean vibrationWarning;
    }
}

package com.locomotive.consume_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "locomotive_summary")
public class LocomotiveSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locomotive_code")
    private String locomotiveCode;

    @Column(name = "vibration_warning")
    private Boolean vibrationWarning;

    @Column(name = "overheat_warning")
    private Boolean overheatWarning;

    @Column(name = "brake_pressure_warning")
    private Boolean brakePressureWarning;

    @Column(name = "brake_pressure_value")
    private Double brakePressureValue;

    @Column(name = "door_status")
    private String doorStatus;

    @Column(name = "engine_temp")
    private Double engineTemp;

    @Column(name = "gps_latitude")
    private Double gpsLatitude;

    @Column(name = "gps_longitude")
    private Double gpsLongitude;

    @Column(name = "gps_elevation")
    private Double gpsElevation;

    @Column(name = "vibration_freq")
    private Integer vibrationFreq;

    @Column(name = "vibration_amplitude")
    private Double vibrationAmplitude;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}


package com.locomotive.consume_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocomotiveSummaryDTO {
    private String locomotiveCode;
    private Boolean vibrationWarning;
    private Boolean overheatWarning;
    private Boolean brakePressureWarning;
    private Double brakePressureValue;
    private String doorStatus;
    private Double engineTemp;
    private Double gpsLatitude;
    private Double gpsLongitude;
    private Double gpsElevation;
    private Integer vibrationFreq;
    private Double vibrationAmplitude;
    private Double speed;
    private LocalDateTime timestamp;
}

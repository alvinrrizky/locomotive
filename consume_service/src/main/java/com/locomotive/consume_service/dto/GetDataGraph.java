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
public class GetDataGraph {
    private String locomotiveCode;
    private Double value;
    private LocalDateTime timestamp;

}

package com.locomotive.consume_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locomotive.consume_service.model.LocomotiveSensor;
import com.locomotive.consume_service.repository.LocomotiveSensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private LocomotiveSensorRepository locomotiveSensorRepository;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @KafkaListener(topics = "locomotive-data", groupId = "groupLocoId")
    public void kafkaListener(String data) {
        System.out.println("Data yang diterima: " + data);

        try {
            // Parsing JSON menjadi objek Locomotive
            LocomotiveSensor locomotive = objectMapper.readValue(data, LocomotiveSensor.class);

            // Simpan ke MongoDB
            locomotiveSensorRepository.save(locomotive);

            System.out.println("Data disimpan ke MongoDB: " + locomotive);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

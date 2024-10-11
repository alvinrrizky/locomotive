package com.locomotive.generate_service.job;

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
import java.util.UUID;

@Component
public class RandomDataJob implements Job {

    @Autowired
    private KafkaService kafkaService;

    private final Random random = new Random();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String[] possibleTrainNames = {"Kereta Api", "Lokomotif", "Gerbong", "KRL", "MRT"};
        String[] possibleStatus = {"Beroperasi", "Diperbaiki", "Kondisi Baik", "Kondisi Buruk"};

        String locoName = possibleTrainNames[random.nextInt(possibleTrainNames.length)];
        String status = possibleStatus[random.nextInt(possibleStatus.length)];

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = currentDateTime.format(formatter);

        Map<String, Object> data = new HashMap<>();
        data.put("TrainCode", UUID.randomUUID());
        data.put("TrainName", locoName);
        data.put("TrainLenght", formatDouble(random.nextDouble() * 100));
        data.put("TrainWidth", formatDouble(random.nextDouble() * 100));
        data.put("TrainHeigth", formatDouble(random.nextDouble() * 100));
        data.put("Status", status);
        data.put("DateAndTime", dateTimeString);

        System.out.println("Generated random data by Quartz: " + data);

        String jsonData = data.toString();
        kafkaService.sendMessage(jsonData);
    }

    private double formatDouble(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

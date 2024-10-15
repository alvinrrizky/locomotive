package com.locomotive.consume_service.service;

import com.locomotive.consume_service.model.LocomotiveSensor;
import com.locomotive.consume_service.model.LocomotiveSummary;
import com.locomotive.consume_service.repository.LocomotiveSensorRepository;
import com.locomotive.consume_service.repository.LocomotiveSummaryRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SummaryService implements Job {

    @Autowired
    LocomotiveSensorRepository locomotiveSensorRepository;

    @Autowired
    LocomotiveSummaryRepository locomotiveSummaryRepository;

    public void sendLatestData() {

        Optional<LocomotiveSummary> latestData = locomotiveSummaryRepository.findLatest();

        if (latestData.isPresent()) {
            sendMessageToTelegramBot(latestData.get());
        }
    }

    private String escapeMarkdown(String text) {
        if (text == null) return "";
        return text.replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.");
    }

    private void sendMessageToTelegramBot(LocomotiveSummary data) {
        TelegramBot bot = new TelegramBot("7725315193:AAH0T7YvG4pFpTAc4Q3ZDxEhmW7T_JgAgvY");

        String message =  "New Data:\n" + "Locomotive Code: " + escapeMarkdown(data.getLocomotiveCode()) + "\nEngine Temperature: " + data.getEngineTemp() + " C" +"\nBrake Pressure: " + data.getBrakePressureValue() + " bar" + "\nVibration Amplitude: " + data.getVibrationAmplitude() + " G-Force";

        SendMessage request = new SendMessage(702081827, message)
                .parseMode(ParseMode.Markdown);

        SendResponse response = bot.execute(request);

        if (!response.isOk()) {
            log.error("Pesan gagal dikirim: {}", response.description());
        }
    }

    public ResponseEntity<String> createSummary() {
        List<LocomotiveSensor> data = locomotiveSensorRepository.findAll();

        for (LocomotiveSensor sensor : data) {
            Optional<LocomotiveSummary> existingSummary = locomotiveSummaryRepository.findByLocomotiveCode(sensor.getLocomotiveCode());

            LocomotiveSummary summary = existingSummary.orElseGet(LocomotiveSummary::new);

            summary.setLocomotiveCode(sensor.getLocomotiveCode());
            summary.setVibrationWarning(sensor.getMaintenanceWarning().isVibrationWarning());
            summary.setOverheatWarning(sensor.getMaintenanceWarning().isOverheatWarning());
            summary.setBrakePressureWarning(sensor.getMaintenanceWarning().isBrakePressureWarning());

            summary.setBrakePressureValue(sensor.getSensorData().getBrakePressure().getValue());
            summary.setDoorStatus(sensor.getSensorData().getDoorStatus().getValue());
            summary.setEngineTemp(sensor.getSensorData().getEngineTemperature().getValue());
            summary.setGpsLatitude(sensor.getSensorData().getGps().getLatitude());
            summary.setGpsLongitude(sensor.getSensorData().getGps().getLongitude());
            summary.setGpsElevation((double) sensor.getSensorData().getGps().getElevation());
            summary.setVibrationFreq(sensor.getSensorData().getVibration().getFrekuensi());
            summary.setVibrationAmplitude(sensor.getSensorData().getVibration().getAmplitudo());
            summary.setSpeed(sensor.getSensorData().getSpeed().getValue());
            summary.setTimestamp(sensor.getTimestamp());

            locomotiveSummaryRepository.save(summary);
        }

        return ResponseEntity.ok("Success");
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            createSummary();
        } catch (Exception e) {
            log.error("Error during summary creation: {}", e.getMessage());
        }

        try {
            sendLatestData();
        } catch (Exception e) {
            log.error("Error sending latest data: {}", e.getMessage());
        }
    }
}

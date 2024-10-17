package com.locomotive.consume_service.service;

import com.locomotive.consume_service.dto.GetAllDataRes;
import com.locomotive.consume_service.dto.GetDataGraph;
import com.locomotive.consume_service.dto.GetDataGraphList;
import com.locomotive.consume_service.dto.LocomotiveSummaryDTO;
import com.locomotive.consume_service.model.LocomotiveSummary;
import com.locomotive.consume_service.repository.LocomotiveSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    LocomotiveSummaryRepository locomotiveSummaryRepository;

    public GetAllDataRes getLocomotiveList() {
        log.info("Start get List Locomotive");
        try {
            var dataList = locomotiveSummaryRepository.findAll();

            Map<String, LocomotiveSummary> latestLocomotiveMap = dataList.stream()
                    .collect(Collectors.groupingBy(
                            LocomotiveSummary::getLocomotiveCode,
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparing(LocomotiveSummary::getTimestamp)),
                                    Optional::get
                            )
                    ));

            List<LocomotiveSummaryDTO> listResponse = latestLocomotiveMap.values().stream()
                    .map(summary -> LocomotiveSummaryDTO.builder()
                            .locomotiveCode(summary.getLocomotiveCode())
                            .vibrationWarning(summary.getVibrationWarning())
                            .overheatWarning(summary.getOverheatWarning())
                            .brakePressureWarning(summary.getBrakePressureWarning())
                            .brakePressureValue(summary.getBrakePressureValue())
                            .doorStatus(summary.getDoorStatus())
                            .engineTemp(summary.getEngineTemp())
                            .gpsLatitude(summary.getGpsLatitude())
                            .gpsLongitude(summary.getGpsLongitude())
                            .gpsElevation(summary.getGpsElevation())
                            .vibrationFreq(summary.getVibrationFreq())
                            .vibrationAmplitude(summary.getVibrationAmplitude())
                            .speed(summary.getSpeed())
                            .timestamp(summary.getTimestamp())
                            .build())
                    .sorted(Comparator.comparing(LocomotiveSummaryDTO::getLocomotiveCode))
                    .collect(Collectors.toList());

            return GetAllDataRes.builder().dataList(listResponse).build();
        } catch (Exception e) {
            log.error("Error retrieving locomotive list: ", e);
            throw e;
        }
    }

    public GetDataGraphList getLocomotiveGraph(String type) {
        log.info("Start get graph data");
        try {
            List<LocomotiveSummary> dataGraphList = locomotiveSummaryRepository.findAll();
            List<GetDataGraph> resultGraphs = new ArrayList<>();

            for (LocomotiveSummary data : dataGraphList) {
                GetDataGraph res = new GetDataGraph();

                res.setLocomotiveCode(data.getLocomotiveCode());
                res.setTimestamp(data.getTimestamp());

                if (type.equals("engine_temp")) {
                    res.setValue(data.getEngineTemp());
                } else if (type.equals("brake_pressure")) {
                    res.setValue(data.getBrakePressureValue());
                } else if (type.equals("vibration_amplitudo")) {
                    res.setValue(data.getVibrationAmplitude());
                }

                resultGraphs.add(res);
            }

            return GetDataGraphList.builder().dataGraphs(resultGraphs).build();
        } catch (Exception e) {
            log.error("Error : ", e);
            throw e;
        }
    }


    public LocomotiveSummary getDetailLocomotive(String locomotiveCode) {
        log.info("Start get detail locomotive for code: {}", locomotiveCode);
        try {
            Optional<LocomotiveSummary> dataSummary = locomotiveSummaryRepository.findLatestByLocomotiveCode(locomotiveCode);

            if (dataSummary.isEmpty()) {
                log.warn("Locomotive not found for code: {}", locomotiveCode);
                return null;
            }

            return dataSummary.get();

        } catch (Exception e) {
            log.error("Error retrieving locomotive details: ", e);
            throw e;
        }
    }
}

package com.apimonitor.backend.service;

import com.apimonitor.backend.entity.ApiDataEntity;
import com.apimonitor.backend.kafka.KafkaProducerService;
import com.apimonitor.backend.repository.ApiDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiMonitorService {

    private final ApiDataRepository repository;
    private final KafkaProducerService kafkaProducer;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.monitor.url}")
    private String apiUrl;

    @Retryable(
            retryFor = {RestClientException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000)
    )
    public String fetchApiData() {
        log.info("Fetching data from: {}", apiUrl);
        return restTemplate.getForObject(apiUrl, String.class);
    }

    @Scheduled(cron = "${api.monitor.cron}")
    public void monitorApi() {
        log.info("Starting scheduled API monitor task");

        try {
            String response = fetchApiData();

            ApiDataEntity entity = ApiDataEntity.builder()
                    .createdAt(Instant.now())
                    .success(true)
                    .payload(response)
                    .build();

            repository.save(entity);
            kafkaProducer.sendSuccessEvent(entity);
            log.info("Successfully saved data with ID: {}", entity.getId());

        } catch (Exception e) {
            log.error("Failed to fetch API data after retries: {}", e.getMessage());

            ApiDataEntity entity = ApiDataEntity.builder()
                    .createdAt(Instant.now())
                    .success(false)
                    .payload("Error: " + e.getMessage())
                    .build();

            repository.save(entity);
            kafkaProducer.sendErrorEvent(e.getMessage());
        }
    }
}
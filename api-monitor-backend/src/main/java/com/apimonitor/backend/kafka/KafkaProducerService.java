package com.apimonitor.backend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private static final String TOPIC_DATA = "api-data";
    private static final String TOPIC_ERRORS = "api-errors";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendSuccessEvent(Object payload) {
        log.info("Sending success event to {}: {}", TOPIC_DATA, payload);
        kafkaTemplate.send(TOPIC_DATA, payload);
    }

    public void sendErrorEvent(String errorMessage) {
        log.error("Sending error event to {}: {}", TOPIC_ERRORS, errorMessage);
        kafkaTemplate.send(TOPIC_ERRORS, errorMessage);
    }
}
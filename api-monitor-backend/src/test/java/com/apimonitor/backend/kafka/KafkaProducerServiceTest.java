package com.apimonitor.backend.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private KafkaProducerService kafkaProducerService;

    @Test
    void sendSuccessEvent_shouldPublishToApiDataTopic() {
        Object payload = new Object();
        kafkaProducerService.sendSuccessEvent(payload);

        verify(kafkaTemplate, times(1)).send(eq("api-data"), eq(payload));
    }

    @Test
    void sendErrorEvent_shouldPublishToApiErrorsTopic() {
        String errorMsg = "Timeout";
        kafkaProducerService.sendErrorEvent(errorMsg);

        verify(kafkaTemplate, times(1)).send(eq("api-errors"), eq(errorMsg));
    }
}
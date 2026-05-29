package com.apimonitor.backend.service;

import com.apimonitor.backend.entity.ApiDataEntity;
import com.apimonitor.backend.kafka.KafkaProducerService;
import com.apimonitor.backend.repository.ApiDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ApiMonitorServiceTest {

    @Mock private ApiDataRepository repository;
    @Mock private KafkaProducerService kafkaProducer;
    @Spy @InjectMocks private ApiMonitorService apiMonitorService;

    @Test
    void monitorApi_shouldSaveSuccessAndSendKafkaEvent() {
        doReturn("{\"bpi\":{\"USD\":{\"rate\":\"65000\"}}}").when(apiMonitorService).fetchApiData();

        apiMonitorService.monitorApi();

        verify(repository, times(1)).save(any(ApiDataEntity.class));
        verify(kafkaProducer, times(1)).sendSuccessEvent(any(ApiDataEntity.class));
        verify(kafkaProducer, never()).sendErrorEvent(anyString());
    }

    @Test
    void monitorApi_shouldSaveFailureAndSendErrorEventOnException() {
        willThrow(new RestClientException("Connection refused")).given(apiMonitorService).fetchApiData();

        apiMonitorService.monitorApi();

        verify(repository).save(argThat(e ->
                !e.getSuccess() && e.getPayload().contains("Error:")));
        verify(kafkaProducer).sendErrorEvent("Connection refused");
    }
}
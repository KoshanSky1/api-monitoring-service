package com.apimonitor.backend.controller;

import com.apimonitor.backend.dto.ApiDataRequest;
import com.apimonitor.backend.entity.ApiDataEntity;
import com.apimonitor.backend.repository.ApiDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiDataController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class ApiDataControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean
    private ApiDataRepository repository;

    @Test
    void getStatus_shouldReturn200Ok() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service is running"));
    }

    @Test
    void getAllData_shouldReturnTop10() throws Exception {
        ApiDataEntity entity = ApiDataEntity.builder()
                .id(UUID.randomUUID()).createdAt(Instant.now()).success(true).payload("{}").build();
        given(repository.findTop10ByOrderByCreatedAtDesc()).willReturn(List.of(entity));

        mockMvc.perform(get("/api/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].success").value(true));
    }

    @Test
    void createData_shouldReturn201CreatedWithLocationHeader() throws Exception {
        ApiDataRequest request = new ApiDataRequest(true, "{\"test\":\"data\"}");
        ApiDataEntity saved = ApiDataEntity.builder()
                .id(UUID.randomUUID()).createdAt(Instant.now()).success(true).payload(request.getPayload()).build();
        given(repository.save(any(ApiDataEntity.class))).willReturn(saved);

        mockMvc.perform(post("/api/data")
                        .with(csrf()) // CSRF нужен, т.к. Security включен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.payload").value("{\"test\":\"data\"}"));
    }

    @Test
    void createData_shouldReturn400OnInvalidPayload() throws Exception {
        ApiDataRequest invalid = new ApiDataRequest(null, "test");

        mockMvc.perform(post("/api/data")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("Поле success обязательно"));
    }
}
package com.apimonitor.backend.controller;

import com.apimonitor.backend.dto.ApiDataRequest;
import com.apimonitor.backend.dto.ApiDataResponse;
import com.apimonitor.backend.entity.ApiDataEntity;
import com.apimonitor.backend.repository.ApiDataRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiDataController {

    private final ApiDataRepository repository;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Service is running");
    }

    @GetMapping("/data")
    public ResponseEntity<List<ApiDataResponse>> getAllData() {
        List<ApiDataEntity> entities = repository.findTop10ByOrderByCreatedAtDesc();
        return ResponseEntity.ok(entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<ApiDataResponse> getDataById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(entity -> ResponseEntity.ok(toResponse(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/data")
    public ResponseEntity<ApiDataResponse> createData(@Valid @RequestBody ApiDataRequest request) {
        ApiDataEntity entity = ApiDataEntity.builder()
                .createdAt(Instant.now())
                .success(request.getSuccess())
                .payload(request.getPayload())
                .build();

        ApiDataEntity saved = repository.save(entity);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PutMapping("/data/{id}")
    public ResponseEntity<ApiDataResponse> updateData(
            @PathVariable UUID id,
            @Valid @RequestBody ApiDataRequest request) {

        return repository.findById(id)
                .map(existing -> {
                    existing.setSuccess(request.getSuccess());
                    existing.setPayload(request.getPayload());
                    ApiDataEntity updated = repository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> deleteData(@PathVariable UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ApiDataResponse toResponse(ApiDataEntity entity) {
        return ApiDataResponse.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .success(entity.getSuccess())
                .payload(entity.getPayload())
                .build();
    }
}
package com.apimonitor.backend.controller;

import com.apimonitor.backend.dto.ApiDataRequest;
import com.apimonitor.backend.dto.ApiDataResponse;
import com.apimonitor.backend.entity.ApiDataEntity;
import com.apimonitor.backend.repository.ApiDataRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "API Data Management", description = "Endpoints for monitoring and managing API response data")
public class ApiDataController {

    private final ApiDataRepository repository;

    @GetMapping("/status")
    @Operation(summary = "Health Check", description = "Returns the current status of the service")
    @ApiResponse(responseCode = "200", description = "Service is running")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Service is running");
    }

    @GetMapping("/data")
    @Operation(summary = "Get Recent Data", description = "Retrieves the 10 most recently created monitoring records")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved data list")
    public ResponseEntity<List<ApiDataResponse>> getAllData() {
        List<ApiDataEntity> entities = repository.findTop10ByOrderByCreatedAtDesc();
        return ResponseEntity.ok(entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/data/{id}")
    @Operation(summary = "Get Data by ID", description = "Retrieves a specific monitoring record by its UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record found and returned"),
            @ApiResponse(responseCode = "404", description = "Record with specified ID not found")
    })
    public ResponseEntity<ApiDataResponse> getDataById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(entity -> ResponseEntity.ok(toResponse(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/data")
    @Operation(summary = "Create Data", description = "Creates a new API monitoring record")
    @ApiResponse(responseCode = "201", description = "Record successfully created")
    public ResponseEntity<ApiDataResponse> createData(@Valid @RequestBody ApiDataRequest request) {
        ApiDataEntity entity = ApiDataEntity.builder()
                .createdAt(Instant.now())
                .success(request.getSuccess())
                .payload(request.getPayload())
                .build();

        ApiDataEntity saved = repository.save(entity);

        // Формируем URI к созданному ресурсу для заголовка Location
        URI location = URI.create("/api/data/" + saved.getId());

        // Возвращаем 201 Created + Location header + тело ответа
        return ResponseEntity.created(location).body(toResponse(saved));
    }

    @PutMapping("/data/{id}")
    @Operation(summary = "Update Data", description = "Updates an existing monitoring record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record successfully updated"),
            @ApiResponse(responseCode = "404", description = "Record with specified ID not found")
    })
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
    @Operation(summary = "Delete Data", description = "Deletes a monitoring record by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Record successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Record with specified ID not found")
    })
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
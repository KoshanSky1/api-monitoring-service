package com.apimonitor.backend.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponse {
    private UUID id;
    private Instant createdAt;
    private Boolean success;
    private String payload;
}

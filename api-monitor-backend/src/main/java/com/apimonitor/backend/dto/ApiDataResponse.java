package com.apimonitor.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ сервера с данными мониторинга")
public class ApiDataResponse {

    @Schema(description = "Уникальный идентификатор записи (UUID)",
            example = "550e8400-e29b-41d4-a716-446655440000",
            accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Schema(description = "Дата и время создания записи в UTC",
            example = "2024-05-20T10:30:00Z",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    @Schema(description = "Флаг успешности выполнения запроса", example = "true")
    private Boolean success;

    @Schema(description = "Полученные данные от внешнего API (JSON строка)",
            example = "{\"bpi\":{\"USD\":{\"code\":\"USD\",\"rate\":\"65,000.00\"}}}")
    private String payload;
}

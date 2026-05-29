package com.apimonitor.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание или обновление данных мониторинга")
public class ApiDataRequest {

    @NotNull(message = "Поле success обязательно")
    @Schema(description = "Результат запроса к внешнему API", example = "true")
    private Boolean success;

    @Schema(description = "Полезный груз (JSON или текст) от внешнего API",
            example = "{\"bpi\":{\"USD\":{\"rate\":\"65000.00\"}}}")
    private String payload;
}
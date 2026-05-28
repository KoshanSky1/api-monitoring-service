package com.apimonitor.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataRequest {
    @NotNull(message = "Поле success обязательно")
    private Boolean success;

    private String payload;
}
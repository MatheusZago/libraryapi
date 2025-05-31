package com.matheusluizago.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

@Tag(name = "Client")
public record ClientDTO(
        @NotBlank(message = "Mandatory field")
        String clientId,
        @NotBlank(message = "Mandatory field")
        String clientSecret,
        @NotBlank(message = "Mandatory field")
        String redirectURI,
        @NotBlank(message = "Mandatory field")
        String scope
) {
}

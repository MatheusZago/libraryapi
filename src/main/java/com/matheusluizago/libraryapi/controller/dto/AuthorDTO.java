package com.matheusluizago.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

//Record já é uma classe que é o próprio construtor
public record AuthorDTO(
        UUID id,
        @NotBlank(message = "Required field.")
        @Size(min = 2, max = 100, message = "Field size invalid.")
        String name,
        @NotNull(message = "Required field.")
        @Past(message = "Date of birth cannot be a future date.")
        LocalDate birthDate,
        @NotBlank(message = "Required field.")
        @Size(min = 2, max = 100, message = "Field size invalid.")
        String nationality) {

}

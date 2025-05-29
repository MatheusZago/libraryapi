        package com.matheusluizago.libraryapi.controller.dto;

        import io.swagger.v3.oas.annotations.media.Schema;
        import jakarta.validation.constraints.Email;
        import jakarta.validation.constraints.NotBlank;

        import java.util.List;

        @Schema(name = "User")
        public record UserDTO(
                @NotBlank(message = "Mandatory field.")
                String login,
                @Email(message = "Invalid email.")
                @NotBlank(message = "Mandatory field.")
                String email,
                @NotBlank(message = "Mandatory field.")
                String password,
                List<String> roles) {

        }

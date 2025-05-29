package com.matheusluizago.libraryapi.controller.dto;

import com.matheusluizago.libraryapi.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Book")
public record RegisterBookDTO(
        @ISBN
        @NotBlank(message = "Required field.")
        String isbn,
        @NotBlank(message = "Required field.")
        String title,
        @NotNull(message = "Required field.")
        @Past(message = "The publish date cannot be a future date.")
        LocalDate publishDate,
        BookGenre genre,
        BigDecimal price,
        @NotNull(message = "Required field.")
        UUID authorId) {


}

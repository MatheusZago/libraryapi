package com.matheusluizago.libraryapi.controller.dto;

import com.matheusluizago.libraryapi.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Book")
public record ResultSearchBookDTO(UUID id,
                                  String isbn,
                                  String title,
                                  LocalDate publishDate,
                                  BookGenre genre,
                                  BigDecimal price,
                                  AuthorDTO author) {
}

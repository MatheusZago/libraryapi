package com.matheusluizago.libraryapi.controller.dto;

import com.matheusluizago.libraryapi.model.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RegisterBookDTO(String isbn,
                              String title,
                              LocalDate publishDate,
                              BookGenre genre,
                              BigDecimal price,
                              UUID authorId) {


}

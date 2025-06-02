package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.model.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.matheusluizago.libraryapi.commom.AuthorDTOConstants.ID_AUTHOR;

public class BookDTOConstants {

    public static final RegisterBookDTO VALID_BOOK_DTO = new RegisterBookDTO(
            "978-006256", // ISBN válido
            "O Hobbit",
            LocalDate.of(1937, 9, 21),
            BookGenre.FANTASY,
            new BigDecimal("49.90"),
            ID_AUTHOR
    );

    public static final RegisterBookDTO INVALID_BOOK_DTO = new RegisterBookDTO(
            "",
            "",
            LocalDate.now().plusDays(1),
            null,
            null,
            null
    );
}

package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.controller.dto.ResultSearchBookDTO;
import com.matheusluizago.libraryapi.model.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.matheusluizago.libraryapi.commom.AuthorDTOConstants.ID_AUTHOR;
import static com.matheusluizago.libraryapi.commom.AuthorDTOConstants.VALID_AUTHOR;

public class BookDTOConstants {

    public static final RegisterBookDTO VALID_BOOK_DTO = new RegisterBookDTO(
            "9783161484100",
            "O Hobbit",
            LocalDate.of(1937, 9, 21),
            BookGenre.FANTASY,
            new BigDecimal("49.90"),
            ID_AUTHOR
    );

    public static final ResultSearchBookDTO VALID_BOOK_SEARCH_DTO = new ResultSearchBookDTO(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "9783161484100",
            "O Hobbit",
            LocalDate.of(1937, 9, 21),
            BookGenre.FANTASY,
            new BigDecimal("49.90"),
            VALID_AUTHOR
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

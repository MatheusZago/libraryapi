package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.matheusluizago.libraryapi.commom.AuthorConstants.VALID_AUTHOR;

public class BookConstants {

    public static final UUID BOOK_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    public static final Book VALID_BOOK = new Book(
            BOOK_ID,
            "978-006256",
            "O Hobbit",
            LocalDate.of(1937, 9, 21),
            BookGenre.FANTASY,
            new BigDecimal("49.90"),
           VALID_AUTHOR
    );

    public static final Book BOOK_NO_ID = new Book(
            null,
            "978-006257",
            "Livro Sem ID",
            LocalDate.of(2000, 1, 1),
            BookGenre.FANTASY,
            new BigDecimal("39.90"),
            VALID_AUTHOR
    );

    public static final Book BOOK_INVALID = new Book(
            null,
            "",
            "",
            LocalDate.now().plusDays(1),
            null,
            null,
            null
    );

}

package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.model.Author;

import java.time.LocalDate;
import java.util.UUID;

public class AuthorConstants {

    public static final UUID AUTHOR_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    public static final Author VALID_AUTHOR = new Author(
            AUTHOR_ID,
            "J.R.R. Tolkien",
            LocalDate.of(1892, 1, 3),
            "British"
    );

    public static final UUID ID_INVALID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final Author INVALID_AUTHOR = new Author(
            ID_INVALID,
            "",
            LocalDate.now().plusDays(1),
            ""
    );

}

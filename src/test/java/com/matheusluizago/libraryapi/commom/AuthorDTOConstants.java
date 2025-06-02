package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;

import java.time.LocalDate;
import java.util.UUID;

public class AuthorDTOConstants {


    public static final UUID ID_AUTHOR = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public static final AuthorDTO VALID_AUTHOR = new AuthorDTO(
            ID_AUTHOR,
            "J.R.R. Tolkien",
            LocalDate.of(1892, 1, 3),
            "British"
    );

}
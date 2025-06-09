package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.controller.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public class UserDtoConstants {

    public static final UUID USER_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");


    public static final UserDTO VALID_USER_DTO = new UserDTO(
            "testuser", "testuser@example.com", "password123", List.of("ROLE_USER"));

    public static final UserDTO INVALID_USER_DTO = new UserDTO(
            "", "", "", List.of()
    );

}

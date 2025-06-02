package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.model.User;

import java.util.List;
import java.util.UUID;

public class UserConstant {

    public static final UUID USER_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    public static final User VALID_USER;

    static {
        VALID_USER = new User();
        VALID_USER.setId(USER_ID);
        VALID_USER.setLogin("testuser");
        VALID_USER.setPassword("password123"); // pode ser criptografada se necessário
        VALID_USER.setEmail("testuser@example.com");
        VALID_USER.setRoles(List.of("ROLE_USER"));
    }
}

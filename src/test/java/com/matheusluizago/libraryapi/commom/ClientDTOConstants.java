package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.controller.dto.ClientDTO;

public class ClientDTOConstants {

    public static final ClientDTO VALID_CLIENT_DTO = new ClientDTO(
            "client-id-123",
            "secret-abc",
            "http://localhost:8080/callback",
            "OPERATOR"
    );

    public static final ClientDTO INVALID_CLIENT_DTO = new ClientDTO(
            "",
            "",
            "",
            ""
    );
}

package com.matheusluizago.libraryapi.commom;

import com.matheusluizago.libraryapi.model.Client;

import java.util.UUID;

public class ClientConstants {

    public static final UUID CLIENT_ID = UUID.randomUUID();
    public static final Client VALID_CLIENT;

    static {
        VALID_CLIENT = new Client();
        VALID_CLIENT.setId(CLIENT_ID);
        VALID_CLIENT.setClientId("client-id-123");
        VALID_CLIENT.setClientSecret("secret-abc");
        VALID_CLIENT.setRedirectURI("http://localhost:8080/callback");
        VALID_CLIENT.setScope("OPERATOR");
    }
}
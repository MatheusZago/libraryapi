package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.service.ClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component //This will be used to return the registered client, for the auth server see if the client is correctly registered
public class CustomRegisteredClientRepository  implements RegisteredClientRepository {

    private final ClientService clientService;
    private final TokenSettings tokenSettings;
    private final ClientSettings clientSettings;

    public CustomRegisteredClientRepository(ClientService clientService, TokenSettings tokenSettings, ClientSettings clientSettings) {
        this.clientService = clientService;
        this.tokenSettings = tokenSettings;
        this.clientSettings = clientSettings;
    }

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client = clientService.getByClientId(clientId);
        RegisteredClient registeredClient;

        if(client.isPresent()){
             registeredClient = RegisteredClient.withId(client.get().getId().toString())
                    .clientId(client.get().getClientId())
                     .clientSecret(client.get().getClientSecret())
                     .redirectUri(client.get().getRedirectURI())
                     .scope(client.get().getScope())
                     .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                     .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                     .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                     .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                     .tokenSettings(tokenSettings)
                     .clientSettings(clientSettings)
                    .build();
        } else {
            return null;
        }

        return registeredClient;
    }
}

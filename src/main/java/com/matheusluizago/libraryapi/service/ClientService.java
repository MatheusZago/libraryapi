package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.repository.ClientRepository;
import com.matheusluizago.libraryapi.validator.ClientValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder encoder;
    private final ClientValidator validator;

    public ClientService(ClientRepository repository, PasswordEncoder encoder, ClientValidator validator) {
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
    }

    public Client save(Client client){
        validator.validate(client);

        var encryptedPassword = encoder.encode(client.getClientSecret());
        client.setClientSecret(encryptedPassword);
        return repository.save(client);
    }

    public Optional<Client> getByClientId(String clientId){
        return repository.findByClientId(clientId);
    }
}

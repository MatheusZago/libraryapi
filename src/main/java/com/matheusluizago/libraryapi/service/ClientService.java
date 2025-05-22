package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder encoder;

    public ClientService(ClientRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public Client save(Client client){
        //*TODO add validation rules to not save duplicate clients and etc.
        var encryptedPassword = encoder.encode(client.getClientSecret());
        client.setClientSecret(encryptedPassword);
        return repository.save(client);
    }

    public Client getByClientId(String clientId){
        return repository.findByClientId(clientId);
    }
}

package com.matheusluizago.libraryapi.validator;

import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.repository.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientValidator {

    private final ClientRepository repository;

    public ClientValidator(ClientRepository repository) {
        this.repository = repository;
    }

    public void validate(Client client){
        if(existsRegisteredClient(client)){
            throw new DuplicateRegisterException("Client already registered.");
        }
    }

    private boolean existsRegisteredClient(Client client){
        Optional<Client> foundClient = repository.findByClientId(client.getClientId());
        return foundClient.isPresent();
    }
}

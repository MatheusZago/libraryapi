package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.commom.ClientConstants;
import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.repository.ClientRepository;
import com.matheusluizago.libraryapi.validator.ClientValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    private ClientService service;
    private ClientRepository repository;
    private PasswordEncoder encoder;
    private ClientValidator validator;

    @BeforeEach()
    public void setUp() {
        repository = Mockito.mock(ClientRepository.class);
        validator = Mockito.mock(ClientValidator.class);
        encoder = Mockito.mock(PasswordEncoder.class);

        service = new ClientService(repository, encoder, validator);
    }

    @Test
    public void saveClient_WithValidData_ReturnClient() {
        Client clientToSave = ClientConstants.VALID_CLIENT;

        when(repository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0)); //returns the passed book as argument

        Client savedClient = service.save(clientToSave);

        verify(validator, times(1)).validate(clientToSave);
        verify(repository, times(1)).save(clientToSave);
    }

    @Test
    public void getById_WhenClientExists_ShouldReturnClient() {
        UUID clientId = ClientConstants.CLIENT_ID;
        Client client = ClientConstants.VALID_CLIENT;

        when(repository.findByClientId(clientId.toString())).thenReturn(Optional.of(client));

        Optional<Client> foundClient = service.getByClientId(clientId.toString());

        verify(repository, times(1)).findByClientId(clientId.toString());
        assertTrue(foundClient.isPresent());
        assertEquals(client, foundClient.get());
    }

}
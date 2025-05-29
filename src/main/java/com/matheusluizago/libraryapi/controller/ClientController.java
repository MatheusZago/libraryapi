package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    //*TODO created DTO and Mapper
    public void save(@RequestBody Client client){
        service.save(client);
    }
}

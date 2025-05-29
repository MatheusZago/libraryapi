package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients")
@Slf4j
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Save", description = "Register a new client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registered with succes."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Client already registered.")
    })
    //*TODO created DTO and Mapper
    public void save(@RequestBody Client client){
        log.info("Registering new client: {} with scope: ", client.getClientId(), client.getScope());
        service.save(client);
    }
}

package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.ClientDTO;
import com.matheusluizago.libraryapi.controller.mappers.ClientMapper;
import com.matheusluizago.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final ClientMapper mapper;

    public ClientController(ClientService service, ClientMapper mapper) {
        this.service = service;
        this.mapper = mapper;;
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

    public void save(@RequestBody @Valid ClientDTO dto){
        var client = mapper.toEntity(dto);
        log.info("Registering new client: {} with scope: {}", client.getClientId(), client.getScope());
        service.save(client);
    }
}

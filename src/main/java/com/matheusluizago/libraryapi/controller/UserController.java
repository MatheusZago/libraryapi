package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.UserDTO;
import com.matheusluizago.libraryapi.controller.mappers.UserMapper;
import com.matheusluizago.libraryapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@Tag(name = "Users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save", description = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registered with succes."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "User  already registered.")
    })
    public void save(@RequestBody @Valid UserDTO dto){
        var user = mapper.toEntity(dto);
        service.save(user);
    }
}

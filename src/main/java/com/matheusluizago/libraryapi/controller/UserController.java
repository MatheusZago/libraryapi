package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.UserDTO;
import com.matheusluizago.libraryapi.controller.mappers.UserMapper;
import com.matheusluizago.libraryapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody UserDTO dto){
        var user = mapper.toEntity(dto);
        service.save(user);
    }
}

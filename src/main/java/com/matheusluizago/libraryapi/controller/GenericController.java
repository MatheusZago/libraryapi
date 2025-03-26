package com.matheusluizago.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {

    //Pra cria rmétodo em interface tem que uasr deafult
    default URI generateHeaderLocation(UUID id){
        //http://localhost:8080/authors/{id}
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

    }
}

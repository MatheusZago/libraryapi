package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service){
        this.service = service;
    }

    @PostMapping
    //Response Entity são os dados de uma resposta (200, 404 e etc)
    //Ta usando void pq nn tem body de resposta
    public ResponseEntity<Void> save(@RequestBody AuthorDTO authorDto){
        var authorEntity = authorDto.mapToAuthor();
        service.save(authorEntity);

        //Criando link pra acessar
        //http://localhost:8080/authors/{id}
        URI localtion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(authorEntity.getId()).toUri();

       return ResponseEntity.created(localtion).build();
    }
}

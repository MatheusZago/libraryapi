package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.service.AuthorService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable("id") String id){
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(idAuthor);

        if(authorOptional.isPresent()){
            Author author = authorOptional.get();
            AuthorDTO dto = new AuthorDTO(author.getId(), author.getName(), author.getBirthdate(), author.getNationality());
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id){
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(idAuthor);

        if(authorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        service.delete(authorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality){

        List<Author> result = service.search(name, nationality);
        //Passando uma transformação pra cada um
        List<AuthorDTO> list = result.stream().map(author ->
                new AuthorDTO(author.getId(), author.getName(), author.getBirthdate(), author.getNationality())
        ).collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

}

package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.controller.dto.ErrorResponse;
import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.exceptions.OperationNotAllowedException;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.service.AuthorService;
import jakarta.validation.Valid;
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
    //Object ta sendo colocado pq ele pode voltar tanto sem nada qnt com o body do erro
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorDTO authorDto){
        try{
            var authorEntity = authorDto.mapToAuthor();
            service.save(authorEntity);

            //Criando link pra acessar
            //http://localhost:8080/authors/{id}
            URI localtion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(authorEntity.getId()).toUri();

            return ResponseEntity.created(localtion).build();
        }catch (DuplicateRegisterException e){
            var errorDTO = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }

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
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
        try {
            var idAuthor = UUID.fromString(id);
            Optional<Author> authorOptional = service.getById(idAuthor);

            if (authorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            service.delete(authorOptional.get());
            return ResponseEntity.noContent().build();
        }catch (OperationNotAllowedException error){
            var errorResponse = ErrorResponse.defaultResponse(error.getMessage());
            return ResponseEntity.status(errorResponse.status()).body(errorResponse);
        }
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

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody AuthorDTO dto){
        try{
            var idAuthor = UUID.fromString(id);
            Optional<Author> authorOptional = service.getById(idAuthor);

            if(authorOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            var author = authorOptional.get();
            author.setName(dto.name());
            author.setNationality(dto.nationality());
            author.setBirthdate(dto.birthDate());

            service.update(author);

            return ResponseEntity.noContent().build();
        }catch (DuplicateRegisterException e){
            var errorDTO = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }

    }

}

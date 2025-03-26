package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.controller.dto.ErrorResponse;
import com.matheusluizago.libraryapi.controller.mappers.AuthorMapper;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController implements GenericController{

    private final AuthorService service;
    private final AuthorMapper mapper;

    public AuthorController(AuthorService service, AuthorMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    //Response Entity são os dados de uma resposta (200, 404 e etc)
    //Object ta sendo colocado pq ele pode voltar tanto sem nada qnt com o body do erro
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorDTO authorDto){
        try{
            Author authorEntity = mapper.toEntity(authorDto);
            service.save(authorEntity);

            //http://localhost:8080/authors/{id}
            URI localtion = generateHeaderLocation(authorEntity.getId());
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

        //Usando mapper
        return service.getById(idAuthor).map(author -> {
            AuthorDTO dto = mapper.toDTO(author);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());

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

        List<Author> result = service.searchByExample(name, nationality);
        //Passando uma transformação pra cada um
        List<AuthorDTO> list = result
                .stream()
                .map(mapper::toDTO)
        .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    private void collect(Collector<Object,?, List<Object>> list) {
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

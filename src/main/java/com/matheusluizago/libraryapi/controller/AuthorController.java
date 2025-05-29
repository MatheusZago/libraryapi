package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.controller.mappers.AuthorMapper;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
@Tag(name = "Authors")
public class AuthorController implements GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    public AuthorController(AuthorService service, AuthorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Save", description = "Register a new author")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registered with succes."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Author already registered.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorDTO authorDto) {

        //Aqui tinha o try catch, mas foi tirado pq as exceções tão sendo lidadas no GlobalExceptionHandelr
        Author authorEntity = mapper.toEntity(authorDto);
        service.save(authorEntity);

        //http://localhost:8080/authors/{id}
        URI localtion = generateHeaderLocation(authorEntity.getId());
        return ResponseEntity.created(localtion).build();

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Get details", description = "Returns author data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found."),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable("id") String id) {
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(idAuthor);

        //Usando mapper
        return service.getById(idAuthor).map(author -> {
            AuthorDTO dto = mapper.toDTO(author);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete", description = "Deletes an existing author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted with success."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "400", description = "Cannot delete an author with a book.")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(idAuthor);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();

        }
        service.delete(authorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Search", description = "Search an author by filter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returning authors.")
    })
    public ResponseEntity<List<AuthorDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality) {

        List<Author> result = service.searchByExample(name, nationality);
        //Passando uma transformação pra cada um
        List<AuthorDTO> list = result
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    private void collect(Collector<Object, ?, List<Object>> list) {
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update", description = "Update an existing author")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Updated with succes."),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody AuthorDTO dto) {
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(idAuthor);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var author = authorOptional.get();
        author.setName(dto.name());
        author.setNationality(dto.nationality());
        author.setBirthdate(dto.birthDate());

        service.update(author);

        return ResponseEntity.noContent().build();

    }

}

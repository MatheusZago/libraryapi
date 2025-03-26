package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.controller.dto.ResultSearchBookDTO;
import com.matheusluizago.libraryapi.controller.mappers.BookMapper;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("books")
public class BookController implements GenericController {

    private final BookService service;
    private final BookMapper mapper;

    public BookController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid RegisterBookDTO bookDTO) {
        Book book = mapper.toEntity(bookDTO);
        service.save(book);

        var url = generateHeaderLocation(book.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultSearchBookDTO> getDetails(@PathVariable("id") String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

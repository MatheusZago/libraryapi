package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.ErrorResponse;
import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.controller.mappers.BookMapper;
import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}

package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.service.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("books ")
public class BookController {

    private final BookService service;

    public BookController(BookService service){
        this.service = service;
    }
}

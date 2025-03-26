package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;

     public BookService(BookRepository repository){
         this.repository = repository;
     }

    public Book save(Book book) {
         return repository.save(book);
    }

    public Optional<Book> getById(UUID id){
         return repository.findById(id);
    }

    public void delete(Book book){
         repository.delete(book);
    }
}

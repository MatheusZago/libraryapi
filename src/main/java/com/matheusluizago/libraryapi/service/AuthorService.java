package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository){
        this.repository = repository;
    }

    public Author save(Author author){
        return repository.save(author);
    }

    public Optional<Author> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Author author) {
        repository.delete(author);
    }
}

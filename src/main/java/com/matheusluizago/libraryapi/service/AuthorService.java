package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.exceptions.OperationNotAllowedException;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.validator.AuthorValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository repository, AuthorValidator validator, BookRepository bookRepository){
        this.repository = repository;
        this.validator = validator;
        this.bookRepository = bookRepository;
    }

    public Author save(Author author){
        validator.validate(author);
        return repository.save(author);
    }

    public void update(Author author){
        if(author.getId() == null){
            throw new IllegalArgumentException("It is necessarity to have the author already in the database to update it.");
        }

        validator.validate(author);
        repository.save(author);
    }

    public Optional<Author> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Author author) {
        if(hasBook(author)){
            throw new OperationNotAllowedException("It is not allowed to delete an author with registered books!");
        }
        repository.delete(author);
    }

    public List<Author> search(String name, String nationality){
        if(name != null && nationality != null){
            return repository.findByNameAndNationality(name, nationality);
        } else if(name != null) {
            return repository.findByName(name);
        } else if(nationality != null){
            return repository.findByNationality(nationality);
        } else {
            return repository.findAll();
        }
    }

    public boolean hasBook(Author author){
        return bookRepository.existsByAuthor(author);
    }

}

package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.exceptions.OperationNotAllowedException;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.security.SecurityService;
import com.matheusluizago.libraryapi.validator.AuthorValidator;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;
    private final SecurityService securityService;

    public AuthorService(AuthorRepository repository, AuthorValidator validator,
                         BookRepository bookRepository, SecurityService securityService){
        this.repository = repository;
        this.validator = validator;
        this.bookRepository = bookRepository;
        this.securityService = securityService;
    }

    public Author save(Author author){
        validator.validate(author);
        Optional<User> user = securityService.getLoggedUser();
        author.setUser(user.orElse(null)); //Getting id for the user for auditing
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

    public List<Author> searchByExample(String name, String nationality){
        var author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues() //Ele desocnsidera qualquer campo que vier nulo
                .withIgnoreCase()
                .withIgnorePaths("id", "dateBirth") //Ele vai ignorar essas coisas se vierem
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); //Pega para se conter o valor, não precisa ser exato
        Example<Author> authorExample = Example.of(author, matcher);

        return repository.findAll(authorExample);
    }

    public boolean hasBook(Author author){
        return bookRepository.existsByAuthor(author);
    }

}

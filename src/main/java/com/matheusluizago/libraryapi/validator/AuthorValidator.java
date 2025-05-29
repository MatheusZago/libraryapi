package com.matheusluizago.libraryapi.validator;

import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private AuthorRepository repository;

    public AuthorValidator(AuthorRepository repository) {
        this.repository = repository;
    }

    public void validate(Author author){
        if(existsRegisteredAuthor(author)){
            throw new DuplicateRegisterException("Author already registered.");
        }

    }

    private boolean existsRegisteredAuthor(Author author) {
        Optional<Author> foundAuthor = repository.findByNameAndBirthdateAndNationality(
                author.getName(), author.getBirthdate(), author.getNationality());

        if (author.getId() == null) {
            return foundAuthor.isPresent();
        }

        // Caso o autor tenha um ID (atualizando), retorna verdadeiro se encontrar um autor com os mesmos dados,
        // mas com um ID diferente
        return foundAuthor.isPresent() && !author.getId().equals(foundAuthor.get().getId());
    }

}

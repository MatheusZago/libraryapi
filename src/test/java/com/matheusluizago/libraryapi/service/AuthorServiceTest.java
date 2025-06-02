package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.repository.AuthorRepository;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.security.SecurityService;
import com.matheusluizago.libraryapi.validator.AuthorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class AuthorServiceTest {

    private AuthorService service;
    private AuthorRepository repository;
    private AuthorValidator validator;
    private BookRepository bookRepository;
    private SecurityService security;

    @BeforeEach
    public void setUp(){
        repository = Mockito.mock(AuthorRepository.class);
        validator = Mockito.mock(AuthorValidator.class);
        security = Mockito.mock(SecurityService.class);
        bookRepository = Mockito.mock(BookRepository.class);

        service = new AuthorService(repository, validator, bookRepository, security);
    }


}

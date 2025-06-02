package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.commom.AuthorConstants;
import com.matheusluizago.libraryapi.commom.BookConstants;
import com.matheusluizago.libraryapi.commom.UserConstant;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.security.SecurityService;
import com.matheusluizago.libraryapi.validator.AuthorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void saveAuthor_WithValidData_ReturnAuthor(){
        Author authorToSave = AuthorConstants.VALID_AUTHOR;
        UUID userId = UserConstant.USER_ID;
        User mockUser = new User();
        mockUser.setId(userId);

        when(security.getLoggedUser()).thenReturn(Optional.of(mockUser)); //simulates logged user
        when(repository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0)); //returns the passed book as argument

        Author savedBook = service.save(authorToSave);

        verify(validator, times(1)).validate(authorToSave);
        verify(repository, times(1)).save(authorToSave);
        assertNotNull(savedBook.getUser());
        assertEquals(userId, savedBook.getUser().getId());
    }


    @Test
    public void getById_WhenAuthorExists_ShouldReturnAuthor(){
        UUID authorId = AuthorConstants.AUTHOR_ID;
        Book book = BookConstants.VALID_BOOK;
        Author author = AuthorConstants.VALID_AUTHOR;

        when(repository.findById(authorId)).thenReturn(Optional.of(author));

        Optional<Author> foundAuthor = service.getById(authorId);

        verify(repository, times(1)).findById(authorId);
        assertTrue(foundAuthor.isPresent());
        assertEquals(author, foundAuthor.get());
    }

    @Test
    public void getById_WhenAuthorDoenstExist_ShouldReturnEmptyOptional(){
        UUID authorId = AuthorConstants.AUTHOR_ID;

        when(repository.findById(authorId)).thenReturn(Optional.empty());

        Optional<Author> foundAuthor = service.getById(authorId);

        verify(repository, times(1)).findById(authorId);
        assertFalse(foundAuthor.isPresent());
    }

    @Test
    public void deleteAuthor_ShouldCallDelete(){
        Author author = AuthorConstants.VALID_AUTHOR;

        service.delete(author);

        verify(repository, times(1)).delete(author);
    }

    @Test
    public void update_WithValidAuthor_ShouldValidateAndSave() {
        Author author = AuthorConstants.VALID_AUTHOR;
        author.setId(UUID.randomUUID());

        service.update(author);

        verify(validator, times(1)).validate(author);
        verify(repository, times(1)).save(author);
    }

    @Test
    public void update_WithNullId_ShouldThrowException() {
        Author author = AuthorConstants.VALID_AUTHOR;
        author.setId(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update(author));
        assertEquals("It is necessarity to have the author already in the database to update it.", exception.getMessage());

        verifyNoInteractions(validator, repository);
    }

}

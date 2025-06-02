package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.commom.BookConstants;
import com.matheusluizago.libraryapi.commom.UserConstant;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.security.SecurityService;
import com.matheusluizago.libraryapi.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookService service;
    private BookRepository repository;
    private BookValidator validator;
    private SecurityService security;

    @BeforeEach
    public void setUp(){
        repository = Mockito.mock(BookRepository.class);
        validator = Mockito.mock(BookValidator.class);
        security = Mockito.mock(SecurityService.class);

        service = new BookService(repository, validator, security);
    }

    @Test
    public void saveBook_WithValidData_ShouldReturnBook(){
        Book bookToSave = BookConstants.VALID_BOOK;
        UUID userId = UserConstant.USER_ID;
        User mockUser = new User();
        mockUser.setId(userId);

        when(security.getLoggedUser()).thenReturn(Optional.of(mockUser)); //simulates logged user
        when(repository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0)); //returns the passed book as argument

        Book savedBook = service.save(bookToSave);

        verify(validator, times(1)).validate(bookToSave);
        verify(repository, times(1)).save(bookToSave);
        assertNotNull(savedBook.getUser());
        assertEquals(userId, savedBook.getUser().getId());
    }

    @Test
    public void getById_WhenBookExists_ShouldReturnBook(){
        UUID bookId = BookConstants.BOOK_ID;
        Book book = BookConstants.VALID_BOOK;

        when(repository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(bookId);

        verify(repository, times(1)).findById(bookId);
        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void getById_WhenBookDoenstExist_ShouldReturnEmptyOptional(){
        UUID bookId = BookConstants.BOOK_ID;

        when(repository.findById(bookId)).thenReturn(Optional.empty());

        Optional<Book> bookFound = service.getById(bookId);

        verify(repository, times(1)).findById(bookId);
        assertFalse(bookFound.isPresent());
    }

    @Test
    public void deleteBook_ShouldCallDelete(){
        Book book = BookConstants.VALID_BOOK;

        service.delete(book);

        verify(repository, times(1)).delete(book);
    }

    @Test
    public void update_WithValidBook_ShouldValidateAndSave() {
        Book book = BookConstants.VALID_BOOK;

        service.update(book);

        verify(validator, times(1)).validate(book);
        verify(repository, times(1)).save(book);
    }

    @Test
    public void update_WithNullId_ShouldThrowException() {
        Book book = BookConstants.VALID_BOOK;
        book.setId(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update(book));
        assertEquals("It is necessarity to have the book already in the database to update it.", exception.getMessage());

        verifyNoInteractions(validator, repository);
    }

    @Test
    public void searchByFilter_ShouldReturnPagedResult() {
        Page<Book> pageResult = Page.empty();

        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageResult);

        Page<Book> search = service.searchByFilter(
                "978-006256",
                "Hobbit",
                "Tolkien",
                BookGenre.FANTASY,
                1937,
                0,
                10
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        assertEquals(pageResult, search);
    }

}

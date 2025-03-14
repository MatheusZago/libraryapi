package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.GenreBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void saveTest(){
        Book book = new Book();
        book.setIsbn("90778-84874");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(GenreBook.FICTION);
        book.setTitle("UFO");
        book.setPublicationDate(LocalDate.of(1980, 1, 2));

        Author author = authorRepository.findById(UUID.fromString(
                "f3043ee6-dd40-48e0-adee-f18ab756deec")).orElse(null);

        book.setAuthor(author);

        bookRepository.save(book);
    }


    @Test
    void saveWithCascadeTest(){
        Book book = new Book();
        book.setIsbn("90778-84874");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(GenreBook.FICTION);
        book.setTitle("UFO");
        book.setPublicationDate(LocalDate.of(1980, 01, 2));

        Author author = new Author();
        author.setName("João");
        author.setNationality("Brasileira");
        author.setBirthdate(LocalDate.of(1950, 01, 31));

        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Test
    void updateBookAuthor(){
        var bookToUpdate = bookRepository.findById(UUID.fromString(
                "bbcc855e-52c7-47de-99b8-a4bfb03c287f")).orElse(null);

        var newAuthor = authorRepository.findById(UUID.fromString(
                "f3043ee6-dd40-48e0-adee-f18ab756deec")).orElse(null);

        bookToUpdate.setAuthor(newAuthor);

        bookRepository.save(bookToUpdate);
    }

    @Test
    void delete(){
        var id = UUID.fromString("ef032e09-c7dd-4bcd-9175-d04dc3afdaa3");
        bookRepository.deleteById(id);
    }

    @Test
    @Transactional
    void findBookTest(){
        UUID id = UUID.fromString("bbcc855e-52c7-47de-99b8-a4bfb03c287f");
        Book book = bookRepository.findById(id).orElse(null);

        System.out.println(book.getTitle());
        System.out.println(book.getAuthor().getName());
    }

}
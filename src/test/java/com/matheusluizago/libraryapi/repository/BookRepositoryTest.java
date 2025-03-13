package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.GenreBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        book.setPublicationDate(LocalDate.of(1980, 01, 2));

        //Tem que passar objeto do tipo author já cadastrado
        Author author = authorRepository.findById(UUID.fromString(
                "f3043ee6-dd40-48e0-adee-f18ab756deec")).orElse(null);
        book.setAuthor(author);

        bookRepository.save(book);
    }

}
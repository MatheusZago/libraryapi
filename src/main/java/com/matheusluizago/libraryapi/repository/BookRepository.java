package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    //Query method
    List<Book> findByAuthor(Author author);

    List<Book> findByTitle(String title);

    List<Book> findByIsbn(String isbn);

    List<Book> findByTitleAndPrice(String title, BigDecimal price);

    List<Book> findByTitleOrIsbn(String title, String isbn);
}

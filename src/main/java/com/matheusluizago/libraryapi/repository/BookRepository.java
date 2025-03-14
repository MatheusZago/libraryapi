package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    //Query method
    List<Book> findByAuthor(Author author);
}

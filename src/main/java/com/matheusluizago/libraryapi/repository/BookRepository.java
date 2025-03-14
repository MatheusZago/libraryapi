package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    //Query method
    List<Book> findByAuthor(Author author);

    List<Book> findByTitle(String title);

    List<Book> findByIsbn(String isbn);

    List<Book> findByTitleAndPrice(String title, BigDecimal price);

    List<Book> findByTitleOrIsbn(String title, String isbn);

    List<Book> findByPublicationDateBetween(LocalDate start, LocalDate end);

    //Tbm tem LesserThan, GreaterThan, GreaterThanEqual, Before e After (pra data)

    //Containing = % valor %, tem endwith e startwith tbm
    List<Book> findByTitleContaining(String title);

    //Pode adicoinar OrderBy(valor) no vim pra vir ordenado
    //Da pra adicionar IgnoreCase no fim tbm
    //Pode adicionar true or false se for algo booleano
    //In, Not in pra inverter
}

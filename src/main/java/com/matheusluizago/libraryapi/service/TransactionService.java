package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.GenreBook;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import com.matheusluizago.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void execute(){
        Author author = new Author();
        author.setName("Francisca");
        author.setNationality("Brasileira");
        author.setBirthdate(LocalDate.of(1950, 01, 31));

        authorRepository.save(author);



        Book book = new Book();
        book.setIsbn("90778-84874");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(GenreBook.FICTION);
        book.setTitle("Livro da Francisca");
        book.setPublicationDate(LocalDate.of(1980, 01, 2));

        book.setAuthor(author);

        bookRepository.save(book);

        //TA FEITO PRA DAR ERRO SÓ PRA MOSTRAR Q N VAI DAR COMMIT
        if(author.getName().equals("Francisca")){
            throw new RuntimeException("Rollback!");
        }
    }
}

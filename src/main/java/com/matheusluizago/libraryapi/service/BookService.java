package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.repository.specs.BookSpecs;
import com.matheusluizago.libraryapi.validator.BookValidator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookValidator validator;

     public BookService(BookRepository repository, BookValidator validator){
         this.repository = repository;
         this.validator = validator;
     }

    public Book save(Book book) {
         validator.validate(book);
         return repository.save(book);
    }

    public Optional<Book> getById(UUID id){
         return repository.findById(id);
    }

    public void delete(Book book){
         repository.delete(book);
    }

    public List<Book> searchByFilter(String isbn, String title, String authorName, BookGenre genre, Integer publishYear){

        //SELECT * FROM book WHERE 0 = 0 //Fazendo uma consulta variavel
        Specification<Book> specs = Specification.where(((root, query, cb) -> cb.conjunction()));

        //Ta adicioando mais coisas na consulta
        if(isbn != null){
            specs = specs.and(BookSpecs.isbnEqual(isbn));
        }

        if(title != null){
            specs = specs.and(BookSpecs.titleLike(title));
        }

        if(genre != null){
            specs = specs.and(BookSpecs.genreEqual(genre));
        }

        if(publishYear != null){
            specs = specs.and(BookSpecs.publishYearEqual(publishYear));
        }

        if(authorName != null){
            specs = specs.and(BookSpecs.nameAuthorLike(authorName));
        }

         return repository.findAll(specs);
    }

    public void update(Book book) {
         if(book.getId() == null) {
             throw new IllegalArgumentException("It is necessarity to have the book already in the database to update it.");
         }

         validator.validate(book);
         repository.save(book);
    }
}

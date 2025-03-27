package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.repository.specs.BookSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;

     public BookService(BookRepository repository){
         this.repository = repository;
     }

    public Book save(Book book) {
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

         return repository.findAll(specs);
    }
}

package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.BookRepository;
import com.matheusluizago.libraryapi.repository.specs.BookSpecs;
import com.matheusluizago.libraryapi.security.SecurityService;
import com.matheusluizago.libraryapi.validator.BookValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookValidator validator;
    private final SecurityService securityService;

     public BookService(BookRepository repository, BookValidator validator, SecurityService securityService){
         this.repository = repository;
         this.validator = validator;
         this.securityService = securityService;

     }

    public Book save(Book book) {
         validator.validate(book);
         Optional<User> user = securityService.getLoggedUser();
         book.setUser(user.orElse(null));
         return repository.save(book);
    }

    public Optional<Book> getById(UUID id){
         return repository.findById(id);
    }

    public void delete(Book book){
         repository.delete(book);
    }

    public Page<Book> searchByFilter(
            String isbn,
            String title,
            String authorName,
            BookGenre genre,
            Integer publishYear,
            Integer page,
            Integer sizePage){

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

        Pageable pageRequest = PageRequest.of(page, sizePage);

         return repository.findAll(specs, pageRequest);
    }

    public void update(Book book) {
         if(book.getId() == null) {
             throw new IllegalArgumentException("It is necessarity to have the book already in the database to update it.");
         }

         validator.validate(book);
         repository.save(book);
    }
}

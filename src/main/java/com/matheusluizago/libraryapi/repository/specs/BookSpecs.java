package com.matheusluizago.libraryapi.repository.specs;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    //Passando pra confirmar se o parametro isbn é igual ao campo do objeto isbn
    public static Specification<Book> isbnEqual(String isbn){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> titleLike(String title){
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("title")) , "%" + title.toUpperCase() + "%");
    }

    public static Specification<Book> genreEqual(BookGenre genre){
        return (root, query, cb) ->
                cb.equal(root.get("genre") , genre);
    }
}

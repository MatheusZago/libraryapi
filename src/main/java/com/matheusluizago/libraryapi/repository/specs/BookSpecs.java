package com.matheusluizago.libraryapi.repository.specs;

import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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

    public static Specification<Book> publishYearEqual(Integer publishYear){
        //Vai usar a função:
        //SELECT TO_CHAR(publish_date, 'YYYY') FROM book;
        return (root, query, cb) -> cb.equal(
                cb.function("to_char", String.class, root.get("publicationDate"), cb.literal("YYYY"))
                , publishYear.toString());
    }

    public static Specification<Book> nameAuthorLike(String name){
        return (root, query, cb) -> {

            //Dessa forma vc controla o join puxando a outra tabela
            Join<Object, Object> joinAuthor = root.join("author", JoinType.INNER);
            return cb.like(cb.upper(joinAuthor.get("name")), "%" + name.toUpperCase() + "%");

//            Forma simples com join sempre
//            return cb.like( cb.upper(root.get("author").get("name")), "%" + name.toUpperCase() + "%");
        };
    }
}

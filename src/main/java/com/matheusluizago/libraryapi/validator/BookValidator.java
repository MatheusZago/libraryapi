package com.matheusluizago.libraryapi.validator;

import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.exceptions.InvalidFieldException;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookValidator {

    private static final int YEAR_PRICE_REQUIRED = 2020;

    private final BookRepository repository;

    public BookValidator(BookRepository repository){
        this.repository = repository;
    }

    public void validate(Book book){
        if(existsBookWithIsbn(book)){
            throw new DuplicateRegisterException("ISBN already registered.");
        }

        if(isPriceObrigatoryNull(book)){
            throw new InvalidFieldException("price", "For books with publish date after 2020 the price is required.");
        }

    }

    //Se um desses for V ele vai voltar como true
    private boolean isPriceObrigatoryNull(Book book) {
        return book.getPrice() == null &&
                book.getPublicationDate().getYear() >= YEAR_PRICE_REQUIRED;

    }

    private boolean existsBookWithIsbn(Book book){
        Optional<Book> foundBook = repository.findByIsbn(book.getIsbn());

        if(book.getId() == null){
            return foundBook.isPresent();
        }

        //Se ele achar qualquer match ele elvta como True e causa o erro
        return foundBook.map(Book::getId).stream().anyMatch(id -> !id.equals(book.getId()));

    }
}

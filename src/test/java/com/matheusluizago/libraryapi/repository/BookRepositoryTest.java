package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void saveTest(){
        Book book = new Book();
        book.setIsbn("90778-84874");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(BookGenre.SCIENCE);
        book.setTitle("SCIENCE BOOK");
        book.setPublicationDate(LocalDate.of(1980, 1, 2));

        Author author = authorRepository.findById(UUID.fromString(
                "f1377abb-c77d-44c4-8f81-9627cfc8ff32")).orElse(null);

        book.setAuthor(author);

        bookRepository.save(book);
    }


    @Test
    void saveWithCascadeTest(){
        Book book = new Book();
        book.setIsbn("90778-84874");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(BookGenre.FICTION);
        book.setTitle("UFO");
        book.setPublicationDate(LocalDate.of(1980, 01, 2));

        Author author = new Author();
        author.setName("João");
        author.setNationality("Brasileira");
        author.setBirthdate(LocalDate.of(1950, 01, 31));

        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Test
    void updateBookAuthor(){
        var bookToUpdate = bookRepository.findById(UUID.fromString(
                "bbcc855e-52c7-47de-99b8-a4bfb03c287f")).orElse(null);

        var newAuthor = authorRepository.findById(UUID.fromString(
                "f3043ee6-dd40-48e0-adee-f18ab756deec")).orElse(null);

        bookToUpdate.setAuthor(newAuthor);

        bookRepository.save(bookToUpdate);
    }

    @Test
    void delete(){
        var id = UUID.fromString("ef032e09-c7dd-4bcd-9175-d04dc3afdaa3");
        bookRepository.deleteById(id);
    }

    @Test
    @Transactional
    void findBookTest(){
        UUID id = UUID.fromString("bbcc855e-52c7-47de-99b8-a4bfb03c287f");
        Book book = bookRepository.findById(id).orElse(null);

        System.out.println(book.getTitle());
        System.out.println(book.getAuthor().getName());
    }

    @Test
    void findBookByTitleTest(){
        List<Book> list = bookRepository.findByTitle("O roubo da casa assombrada");
        list.forEach(System.out::println);
    }

    @Test
    void findBookByIsbnTest(){
        List<Book> list = bookRepository.findByIsbn("99999-84874");
        list.forEach(System.out::println);
    }

    @Test
    void findBookByTitleAndPriceTest(){
        var price = BigDecimal.valueOf(204.00);

        List<Book> list = bookRepository.findByTitleAndPrice(
                "O roubo da casa assombrada", price);
        list.forEach(System.out::println);
    }

    @Test
    void findBookByTitleOrIsbnTest(){

        List<Book> list = bookRepository.findByTitleOrIsbn(
                "O roubo casa assombrada", "99999-84874" );
        list.forEach(System.out::println);
    }

    @Test
    void findBookByPublishedDateBetweenTest(){

        List<Book> list = bookRepository.findByPublicationDateBetween(
                LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1));
        list.forEach(System.out::println);
    }

    @Test
    void findByTitleContainingTest(){
        List<Book> list = bookRepository.findByTitleContaining("casa assombrada");

        list.forEach(System.out::println);
    }

    @Test
    void listAllWithQuery(){
        var result = bookRepository.listAllOrderByTitleAndPrice();
        result.forEach(System.out::println);
    }

    @Test
    @Transactional
    void listBookAuthors(){
        var result = bookRepository.listBookAuthors();
        result.forEach(System.out::println);
    }

    @Test
    void listTitleBooksTest(){
        var result = bookRepository.listTitleBooks();
        result.forEach(System.out::println);
    }

    @Test
    void listGenreBrasilianAuthorTest(){
        var result = bookRepository.listGenreBrasilianAuthors();
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreQueryParamTest(){
        var result = bookRepository.findByGenre(BookGenre.FICTION, "price");
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreQueryParamPositionalTest(){
        var result = bookRepository.findByGenrePositional(BookGenre.FICTION, "price");
        result.forEach(System.out::println);
    }

    @Test
    void deleteByGenreTest(){
        bookRepository.deleteByGenre(BookGenre.SCIENCE);
    }

    @Test
    void updatePublicationDateTest(){
        bookRepository.updateDataPublication(LocalDate.of(2000, 01, 01));
    }

}
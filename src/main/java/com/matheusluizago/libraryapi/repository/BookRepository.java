package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>,
        JpaSpecificationExecutor<Book> //pra pesquisas mais especificas
         {

    //Query method
    List<Book> findByAuthor(Author author);

    List<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

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

    // JPQL -> referencia as entidades e as propriedades
    // select l.* from livro as l order by l.titulo
    @Query(" select b from Book as b order by b.title, b.price ")
    List<Book> listAllOrderByTitleAndPrice();

     /* select a.*
            * from livro l
     * join autor a on a.id = l.id_autor
     */
    @Query("SELECT a FROM Book b JOIN b.author a ")
    List<Author> listBookAuthors();

    @Query("SELECT DISTINCT b.title FROM Book b")
    List<String> listTitleBooks();

    @Query("""
            SELECT b.genre
            FROM Book b
            JOIN b.author a
            WHERE a.nationality = 'Mineiro'
            ORDER BY b.genre
            """)
    List<String> listGenreBrasilianAuthors();

    //Named Parameters
    @Query("SELECT b FROM Book b WHERE b.genre = :genre ORDER BY :param")
    List<Book> findByGenre(@Param("genre") BookGenre bookGenre, @Param("param") String param);

    //Positional parameters
    @Query("SELECT b FROM Book b WHERE b.genre = ?1 ORDER BY ?2")
    List<Book> findByGenrePositional(BookGenre bookGenre, String param);

    @Modifying
    @Transactional
    @Query("DELETE FROM Book WHERE genre = ?1")
    void deleteByGenre(BookGenre genre);

    @Modifying
    @Transactional //Só pra teste, SEMPRE coloque WHERE
    @Query("UPDATE Book SET publicationDate = ?1")
    void updateDataPublication(LocalDate newDate);

    boolean existsByAuthor(Author author);


}

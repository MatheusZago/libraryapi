package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

	@Autowired
	BookRepository bookRepository;

    @Test
	void saveTest(){
		Author author = new Author();
		author.setName("Maria");
		author.setNationality("Brasileira");
		author.setBirthdate(LocalDate.of(1950, 01, 31));

		var savedAuthor = authorRepository.save(author);
		System.out.print(savedAuthor);
    }

	@Test
	void updateTest(){
		var id = UUID.fromString("570b091d-c810-4edd-9279-5f7dea1ceeb0");

		Optional<Author> possibleAuthor = authorRepository.findById(id);

		if(possibleAuthor.isPresent()){

			Author author = possibleAuthor.get();
			System.out.println("Author data:");
			System.out.println(author);

			author.setBirthdate(LocalDate.of(1960, 1, 31));

			authorRepository.save(author);
		}
	}

	@Test
	void listTest(){
		List<Author> list = authorRepository.findAll();
		list.forEach(System.out::println);
	}

	@Test
	void count(){
		System.out.println("Author's count: " + authorRepository.count());
	}

	@Test
	void deleteByIdTest(){
		var id = UUID.fromString("570b091d-c810-4edd-9279-5f7dea1ceeb0");
		authorRepository.deleteById(id);
	}

	@Test
	void saveAuthorWithBooksTest(){
		Author author = new Author();
		author.setName("Antonio");
		author.setNationality("Americano");
		author.setBirthdate(LocalDate.of(1970, 5, 12));

		Book book = new Book();
		book.setIsbn("99999-84874");
		book.setPrice(BigDecimal.valueOf(204));
		book.setGenre(BookGenre.MYSTERY);
		book.setTitle("O roubo da casa assombrada");
		book.setPublicationDate(LocalDate.of(1999, 3, 13));
		book.setAuthor(author);

		//Botando livros na lsita do author

		Book book2 = new Book();
		book2.setIsbn("99999-84874");
		book2.setPrice(BigDecimal.valueOf(204));
		book2.setGenre(BookGenre.MYSTERY);
		book2.setTitle("O roubo da casa assombrada");
		book2.setPublicationDate(LocalDate.of(1999, 3, 13));
		book2.setAuthor(author);

		//Botando livros na lsita do author
		author.setBooks(new ArrayList<>());
		author.getBooks().add(book);
		author.getBooks().add(book2);

		authorRepository.save(author);
//		bookRepository.saveAll(author.getBooks());
	}

	@Test
//	@Transactional
	void listAuthorBooks(){
		var id = UUID.fromString("82525c90-f2af-487f-a16d-c54662fff99c");
		var author = authorRepository.findById(id).get();

		//Sem usar transactional, quer buscar os livros do authro

		List<Book> bookList = bookRepository.findByAuthor(author);
		author.setBooks(bookList);

		author.getBooks().forEach(System.out::println);
	}

}

package com.matheusluizago.libraryapi.repositories;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void saveTest(){
		Author author = new Author();
		author.setName("Maria");
		author.setNationality("Brasileira");
		author.setBirthdate(LocalDate.of(1950, 01, 31));

		var savedAuthor = authorRepository.save(author);
		System.out.print(savedAuthor);
    }

	@Test
	public void updateTest(){
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
	public void listTest(){
		List<Author> list = authorRepository.findAll();
		list.forEach(System.out::println);
	}

	@Test
	public void count(){
		System.out.println("Author's count: " + authorRepository.count());
	}

	@Test
	public void deleteByIdTest(){
		var id = UUID.fromString("570b091d-c810-4edd-9279-5f7dea1ceeb0");
		authorRepository.deleteById(id);
	}

}

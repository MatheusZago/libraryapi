package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import com.matheusluizago.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void updatingWithoutUpdate(){
        var book  = bookRepository.
                findById(UUID.fromString("bbcc855e-52c7-47de-99b8-a4bfb03c287f"))
                .orElse(null);

        book.setPublishDate(LocalDate.of(2004, 6, 13));

//        bookRepository.save(book); //Não precisa disso pq tem o transational
    }

    //Pseudocódigo só pra exemplo
    @Transactional
    public void saveBookWithPicture(){
        //salva o livro
        //repository.save(book)

        //pega o id do livro
        //var id = book.getId(); //Msm se ainda nn tiver tido o commit o jpa já criou o id

        //salva foto do livro numa bucket
        //bucketService.salvar(book.getFoto(), id + ".png");

        //Atualizar o nome do arquivo que foi salvo
        //book.setNameArquivePicture(id + ".png");
    }

    @Transactional
    public void execute(){
        Author author = new Author();
        author.setName("Francisca");
        author.setNationality("Brasileira");
        author.setBirthdate(LocalDate.of(1950, 01, 31));

        authorRepository.save(author);



        Book book = new Book();
        book.setIsbn("90778-84874");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(BookGenre.FICTION);
        book.setTitle("Livro da Francisca");
        book.setPublishDate(LocalDate.of(1980, 01, 2));

        book.setAuthor(author);

        bookRepository.save(book);

        //TA FEITO PRA DAR ERRO SÓ PRA MOSTRAR Q N VAI DAR COMMIT
        if(author.getName().equals("Francisca")){
            throw new RuntimeException("Rollback!");
        }
    }
}

package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.controller.dto.ResultSearchBookDTO;
import com.matheusluizago.libraryapi.controller.mappers.BookMapper;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.service.BookService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("books")
public class BookController implements GenericController {

    private final BookService service;
    private final BookMapper mapper;

    public BookController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid RegisterBookDTO bookDTO) {
        Book book = mapper.toEntity(bookDTO);
        service.save(book);

        var url = generateHeaderLocation(book.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultSearchBookDTO> getDetails(@PathVariable("id") String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    service.delete(book);
                    return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResultSearchBookDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "name-author", required = false)
            String nameAuthor,
            @RequestParam(value = "genre", required = false)
            BookGenre genre,
            @RequestParam(value = "publish-year", required = false)
            Integer publishYear){

        var result = service.searchByFilter(isbn, title, nameAuthor, genre, publishYear);
        var list = result.stream().map(mapper:: toDTO).collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }



}

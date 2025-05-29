package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.controller.dto.ResultSearchBookDTO;
import com.matheusluizago.libraryapi.controller.mappers.BookMapper;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("books")
@Tag(name = "Books")
public class BookController implements GenericController {

    private final BookService service;
    private final BookMapper mapper;

    public BookController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Save", description = "Register a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registered with succes."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Book already registered.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid RegisterBookDTO bookDTO) {
        Book book = mapper.toEntity(bookDTO);
        service.save(book);

        var url = generateHeaderLocation(book.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Get details", description = "Returns book data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    public ResponseEntity<ResultSearchBookDTO> getDetails(@PathVariable("id") String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @DeleteMapping("{id}")
    @Operation(summary = "Delete", description = "Deletes an existing book.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted with success."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    service.delete(book);
                    return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Search", description = "Search an book by filter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returning books.")
    })
    public ResponseEntity<Page<ResultSearchBookDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "name-author", required = false)
            String nameAuthor,
            @RequestParam(value = "genre", required = false)
            BookGenre genre,
            @RequestParam(value = "publish-year", required = false)
            Integer publishYear,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer sizePage){

        Page<Book> pageResult = service.searchByFilter(isbn, title, nameAuthor, genre, publishYear, page, sizePage);

        Page<ResultSearchBookDTO> result = pageResult.map(mapper::toDTO);


        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @PutMapping("{id}")
    @Operation(summary = "Update", description = "Update an existing book")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Updated with succes."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody @Valid RegisterBookDTO bookDTO){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    Book entityAux = mapper.toEntity(bookDTO);
                    book.setAuthor(entityAux.getAuthor());
                    book.setPrice(entityAux.getPrice());
                    book.setIsbn(entityAux.getIsbn());
                    book.setPublishDate(entityAux.getPublishDate());
                    book.setTitle(entityAux.getTitle());

                    service.update(book);

                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

}

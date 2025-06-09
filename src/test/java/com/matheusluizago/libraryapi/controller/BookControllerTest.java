package com.matheusluizago.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusluizago.libraryapi.commom.BookConstants;
import com.matheusluizago.libraryapi.commom.BookDTOConstants;
import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.controller.dto.ResultSearchBookDTO;
import com.matheusluizago.libraryapi.controller.mappers.BookMapper;
import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.model.BookGenre;
import com.matheusluizago.libraryapi.service.BookService;
import com.matheusluizago.libraryapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BookController.class)
@AutoConfigureDataJpa
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @MockBean
    private BookMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityFilterChain filterChain;

    private UUID id;
    private RegisterBookDTO registerDTO;
    private Book book;
    private ResultSearchBookDTO resultDTO;
    private RegisterBookDTO invalidDTO;

    @BeforeEach
    void setup(){
        id = BookDTOConstants.VALID_BOOK_DTO.authorId();
        registerDTO = BookDTOConstants.VALID_BOOK_DTO;
        book = BookConstants.VALID_BOOK;
        resultDTO = BookDTOConstants.VALID_BOOK_SEARCH_DTO;
        invalidDTO = BookDTOConstants.INVALID_BOOK_DTO;
    }

    @Test
    public void saveBook_WithValidData_Returns201() throws Exception {

        Mockito.when(mapper.toEntity(registerDTO)).thenReturn(book);
        Mockito.when(service.save(book)).thenReturn(book);

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/books/" + book.getId()));
    }

    @Test
    public void saveBook_WithInvalidData_Returns422() throws Exception {
        RegisterBookDTO invalidDTO = BookDTOConstants.INVALID_BOOK_DTO;

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void saveBook_AlreadyExists_Returns409() throws Exception {
        Mockito.when(mapper.toEntity(registerDTO)).thenReturn(book);
        Mockito.when(service.save(book)).thenThrow(new DuplicateRegisterException("Book already exists"));

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Book already exists"));
    }

    @Test
    public void getDetails_WithValidId_Returns200() throws Exception {
        Mockito.when(service.getById(id)).thenReturn(Optional.of(book));
        Mockito.when(mapper.toDTO(book)).thenReturn(resultDTO);

        mockMvc.perform(get("/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(resultDTO.title()))
                .andExpect(jsonPath("$.isbn").value(resultDTO.isbn()))
                .andExpect(jsonPath("$.author.name").value(resultDTO.author().name()));
    }

    @Test
    public void getDetails_BookNotFound_Returns404() throws Exception {
        Mockito.when(service.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteBook_WithValidId_Returns204() throws Exception {
        Mockito.when(service.getById(id)).thenReturn(Optional.of(book));
        Mockito.doNothing().when(service).delete(book);

        mockMvc.perform(delete("/books/" + id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteBook_NotFound_Returns404() throws Exception {
        Mockito.when(service.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/books/" + id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    //TODO Fix the search tests
    @Test
    public void searchBooks_WithoutFilters_Returns200WithEmptyList() throws Exception {
        Page<ResultSearchBookDTO> emptyPage = new PageImpl<>(List.of());

        Mockito.when(service.searchByFilter(null, null, null, null, null, 0, 10))
                .thenReturn(new PageImpl<>(List.of()));

        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(BookDTOConstants.VALID_BOOK_SEARCH_DTO);

        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("page-size", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }



    @Test
    public void searchBooks_WithFilters_Returns200WithResults() throws Exception {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        Mockito.when(service.searchByFilter("9783161484100", "Test Book", "Author Name", BookGenre.FANTASY, 2024, 0, 10))
                .thenReturn(bookPage);

        Mockito.when(mapper.toDTO(book)).thenReturn(resultDTO);

        mockMvc.perform(get("/books")
                        .param("isbn", "9783161484100")
                        .param("title", "Test Book")
                        .param("name-author", "Author Name")
                        .param("genre", "FANTASY")
                        .param("publish-year", "2024")
                        .param("page", "0")
                        .param("page-size", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(resultDTO.id().toString()))
                .andExpect(jsonPath("$.content[0].title").value(resultDTO.title()));
    }

    @Test
    public void updateBook_WithValidData_Returns204() throws Exception {
        Book existingBook = BookConstants.VALID_BOOK;
        Book mappedBook = BookConstants.VALID_BOOK;

        Mockito.when(service.getById(id)).thenReturn(Optional.of(existingBook));
        Mockito.when(mapper.toEntity(registerDTO)).thenReturn(mappedBook);
        Mockito.doNothing().when(service).update(Mockito.any(Book.class));

        mockMvc.perform(put("/books/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isNoContent());
    }


    @Test
    public void updateBook_BookNotFound_Returns404() throws Exception {
        Mockito.when(service.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(put("/books/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBook_WithInvalidData_Returns422() throws Exception {
        mockMvc.perform(put("/books/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isUnprocessableEntity());
    }



}

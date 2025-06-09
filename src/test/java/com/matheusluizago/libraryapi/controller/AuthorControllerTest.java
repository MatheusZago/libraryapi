package com.matheusluizago.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusluizago.libraryapi.commom.AuthorConstants;
import com.matheusluizago.libraryapi.commom.AuthorDTOConstants;
import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.controller.mappers.AuthorMapper;
import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.Author;
import com.matheusluizago.libraryapi.service.AuthorService;
import com.matheusluizago.libraryapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(AuthorController.class)
@AutoConfigureDataJpa() //Load JPA in order to let the test work (it is only loaded in the app)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService service;

    @MockBean
    private AuthorMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityFilterChain filterChain;

    private UUID idAuthor;
    private AuthorDTO authorDTO;
    private Author author;
    private AuthorDTO invalidAuthorDTO;

    @BeforeEach
    public void setUp(){
        idAuthor = AuthorDTOConstants.ID_AUTHOR;
         authorDTO = AuthorDTOConstants.VALID_AUTHOR;
         author = AuthorConstants.VALID_AUTHOR;
         invalidAuthorDTO = AuthorDTOConstants.INVALID_AUTHOR;
    }

    @Test
    public void saveAuthor_WithValidData_Return201() throws Exception {
        Mockito.when(mapper.toEntity(authorDTO)).thenReturn(author);

        Mockito.when(service.save(author)).thenReturn(author);

        mockMvc.perform(post("/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/authors/" + idAuthor));

    }

    @Test
    public void saveAuthor_WithInvalidEmail_Return422() throws Exception {
        mockMvc.perform(post("/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthorDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void saveAuthor_AlreadyRegistered_Return409() throws Exception {
        Mockito.when(mapper.toEntity(authorDTO)).thenReturn(author);

        Mockito.when(service.save(author)).thenThrow(new DuplicateRegisterException("Author already registered"));

        mockMvc.perform(post("/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Author already registered"));
    }

    @Test
    public void getDetails_WithValidRole_Returns200() throws Exception {
        Mockito.when(service.getById(idAuthor)).thenReturn(Optional.of(author));
        Mockito.when(mapper.toDTO(author)).thenReturn(authorDTO);

        mockMvc.perform(get("/authors/" + idAuthor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("J.R.R. Tolkien"));
    }


    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void getDetails_AuthorNotFound_Returns404() throws Exception {
        Mockito.when(service.getById(idAuthor)).thenReturn(Optional.empty());

        mockMvc.perform(get("/authors/" + idAuthor))
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_WithManagerRole_ExistingAuthor_Returns204() throws Exception {
        UUID id = AuthorConstants.VALID_AUTHOR.getId();
        Author author = AuthorConstants.VALID_AUTHOR;

        Mockito.when(service.getById(id)).thenReturn(Optional.of(author));

        mockMvc.perform(delete("/authors/" + id))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(author);
    }

    @Test
    public void delete_WithManagerRole_NonExistingAuthor_Returns404() throws Exception {
        Mockito.when(service.getById(idAuthor)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/authors/" + idAuthor))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.never()).delete(Mockito.any());
    }

    @Test
    public void delete_WithManagerRole_AuthorWithBook_Returns400() throws Exception {
        Mockito.when(service.getById(idAuthor)).thenReturn(Optional.of(author));
        Mockito.doThrow(new IllegalStateException("Author has books"))
                .when(service).delete(author);

        mockMvc.perform(delete("/authors/" + idAuthor))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void search__Returns200() throws Exception {
        List<Author> authors = List.of(AuthorConstants.VALID_AUTHOR);
        Mockito.when(service.searchByExample(null, null)).thenReturn(authors);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(AuthorDTOConstants.VALID_AUTHOR);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(AuthorDTOConstants.VALID_AUTHOR.name()));
    }

    @Test
    public void search_WithFilters_Returns200() throws Exception {
        List<Author> authors = List.of(AuthorConstants.VALID_AUTHOR);
        String name = "J.R.R. Tolkien";
        String nationality = "British";

        Mockito.when(service.searchByExample(name, nationality)).thenReturn(authors);
            Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(AuthorDTOConstants.VALID_AUTHOR);

        mockMvc.perform(get("/authors")
                        .param("name", name)
                        .param("nationality", nationality))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @Test
    public void update_WithValidData_Returns204() throws Exception {
        Author existingAuthor = AuthorConstants.VALID_AUTHOR;
        AuthorDTO updatedDTO = new AuthorDTO(UUID.randomUUID(), "J.R.R. Tolkien",  LocalDate.of(1892, 1, 3), "British");


        Mockito.when(service.getById(idAuthor)).thenReturn(Optional.of(existingAuthor));

        mockMvc.perform(put("/authors/" + idAuthor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isNoContent());

        Mockito.verify(service).update(Mockito.any(Author.class));
    }



    @Test
    public void update_WithInvalidDTO_Returns422() throws Exception {
        Author existingAuthor = AuthorConstants.VALID_AUTHOR;
        Mockito.when(service.getById(idAuthor)).thenReturn(Optional.of(existingAuthor));

        AuthorDTO invalidDTO = new AuthorDTO(UUID.randomUUID(), "", LocalDate.of(1892, 1, 3), "British");

        mockMvc.perform(put("/authors/" + idAuthor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isUnprocessableEntity());
    }


}

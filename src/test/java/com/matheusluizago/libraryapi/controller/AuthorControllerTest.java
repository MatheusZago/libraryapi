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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    @WithMockUser
    public void saveAuthor_WithValidData_UserAuthorized_Return201() throws Exception {
        UUID id = AuthorDTOConstants.ID_AUTHOR;
        AuthorDTO authorDTO = AuthorDTOConstants.VALID_AUTHOR;
        Author author = AuthorConstants.VALID_AUTHOR;

        Mockito.when(mapper.toEntity(authorDTO)).thenReturn(author);

        Mockito.when(service.save(author)).thenReturn(author);

        mockMvc.perform(post("/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/authors/" + id));

    }

    @Test
    public void saveAuthor_WithValidData_UserNotAuthorized_Return403() throws Exception {
        UUID id = AuthorDTOConstants.ID_AUTHOR;
        AuthorDTO authorDTO = AuthorDTOConstants.VALID_AUTHOR;
        Author author = AuthorConstants.VALID_AUTHOR;

        Mockito.when(mapper.toEntity(authorDTO)).thenReturn(author);

        Mockito.when(service.save(author)).thenReturn(author);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void saveAuthor_WithInvalidEmail_Return422() throws Exception {
        AuthorDTO invalidAuthorDTO = AuthorDTOConstants.INVALID_AUTHOR;

        mockMvc.perform(post("/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthorDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser
    public void saveAuthor_AlreadyRegistered_Return409() throws Exception {
        AuthorDTO authorDTO = AuthorDTOConstants.VALID_AUTHOR;
        Author author = AuthorConstants.VALID_AUTHOR;

        Mockito.when(mapper.toEntity(authorDTO)).thenReturn(author);

        Mockito.when(service.save(author)).thenThrow(new DuplicateRegisterException("Author already registered"));

        mockMvc.perform(post("/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Author already registered"));
    }

}

package com.matheusluizago.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusluizago.libraryapi.commom.UserConstant;
import com.matheusluizago.libraryapi.commom.UserDtoConstants;
import com.matheusluizago.libraryapi.controller.dto.UserDTO;
import com.matheusluizago.libraryapi.controller.mappers.UserMapper;
import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;
    @MockBean
    private SecurityFilterChain filterChain;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDTO userDTO;
    private UserDTO invalidUserDTO;

    @BeforeEach
    public void setUp(){
        user = UserConstant.VALID_USER;
        userDTO = UserDtoConstants.VALID_USER_DTO;
        invalidUserDTO = UserDtoConstants.INVALID_USER_DTO;
    }

    @Test
    void save_ValidClientDTO_ReturnsCreated() throws Exception {
        Mockito.when(mapper.toEntity(userDTO)).thenReturn(user);
        Mockito.when(service.save(user)).thenReturn(user);


        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void save_DuplicateClient_Returns409() throws Exception {
        Mockito.when(mapper.toEntity(userDTO)).thenReturn(user);
        Mockito.when(service.save(user)).thenThrow(new DuplicateRegisterException("User already registered"));

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void save_InvalidClientDTO_Returns422() throws Exception {
        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().isUnprocessableEntity());
    }
}

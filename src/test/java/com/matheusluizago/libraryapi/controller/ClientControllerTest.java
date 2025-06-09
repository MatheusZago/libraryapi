package com.matheusluizago.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusluizago.libraryapi.commom.ClientConstants;
import com.matheusluizago.libraryapi.commom.ClientDTOConstants;
import com.matheusluizago.libraryapi.controller.dto.ClientDTO;
import com.matheusluizago.libraryapi.controller.mappers.ClientMapper;
import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.service.ClientService;
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
@WebMvcTest(ClientController.class)
@AutoConfigureDataJpa
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;

    @MockBean
    private ClientMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityFilterChain filterChain;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;
    private ClientDTO clientDTO;
    private ClientDTO invalidClientDTO;

    @BeforeEach
    public void setUp(){
        client = ClientConstants.VALID_CLIENT;
        clientDTO = ClientDTOConstants.VALID_CLIENT_DTO;
        invalidClientDTO = ClientDTOConstants.INVALID_CLIENT_DTO;
    }

    @Test
    void save_ValidClientDTO_ReturnsCreated() throws Exception {
        Mockito.when(mapper.toEntity(clientDTO)).thenReturn(client);
        Mockito.when(service.save(client)).thenReturn(client);


        mockMvc.perform(post("/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void save_DuplicateClient_Returns409() throws Exception {
        Mockito.when(mapper.toEntity(clientDTO)).thenReturn(client);
        Mockito.when(service.save(client)).thenThrow(new DuplicateRegisterException("Client already registered"));

        mockMvc.perform(post("/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void save_InvalidClientDTO_Returns422() throws Exception {
        mockMvc.perform(post("/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidClientDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

}

package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.commom.ClientConstants;
import com.matheusluizago.libraryapi.commom.UserConstant;
import com.matheusluizago.libraryapi.model.Client;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.UserRepository;
import com.matheusluizago.libraryapi.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository repository;
    private PasswordEncoder encoder;
    private UserValidator validator;
    private UserService service;

    @BeforeEach
    public void setUp(){
        this.repository = Mockito.mock(UserRepository.class);
        this.encoder = Mockito.mock(PasswordEncoder.class);
        this.validator = Mockito.mock(UserValidator.class);

        service = new UserService(repository, encoder, validator);
    }

    @Test
    public void saveUser_WithValidData_ReturnUser() {
        User userToSave = UserConstant.VALID_USER;

        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); //returns the passed book as argument

        User savedUser = service.save(userToSave);

        verify(validator, times(1)).validate(userToSave);
        verify(repository, times(1)).save(userToSave);
    }

    @Test
    public void getByLogin_WhenUserExists_ShouldReturnUser() {
        String login = UserConstant.VALID_USER.getLogin();
        User user = UserConstant.VALID_USER;

        when(repository.findByLogin(login)).thenReturn(Optional.of(user));

        Optional<User> foundUser = service.getByLogin(user.getLogin());

        verify(repository, times(1)).findByLogin(login);
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void getByLogin_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        String login = "nonexistent_user";

        when(repository.findByLogin(login)).thenReturn(Optional.empty());

        Optional<User> foundUser = service.getByLogin(login);

        verify(repository, times(1)).findByLogin(login);
        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void getByEmail_WhenUserExists_ShouldReturnUser() {
        String email = UserConstant.VALID_USER.getEmail();
        User user = UserConstant.VALID_USER;

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> foundUser = service.getByEmail(user.getEmail());

        verify(repository, times(1)).findByEmail(email);
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

}

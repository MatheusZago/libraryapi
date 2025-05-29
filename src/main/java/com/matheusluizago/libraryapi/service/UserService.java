package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.UserRepository;
import com.matheusluizago.libraryapi.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserValidator validator;

    public UserService(UserRepository repository, PasswordEncoder encoder, UserValidator validator) {
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
    }

    public void save(User user){
        validator.validate(user);
        var password = user.getPassword();
        user.setPassword(encoder.encode(password));
        repository.save(user);
    }

    public Optional<User> getByLogin(String login){
        return repository.findByLogin(login);
    }

    public User getByEmail(String email){
        return repository.findByEmail(email);
    }
}

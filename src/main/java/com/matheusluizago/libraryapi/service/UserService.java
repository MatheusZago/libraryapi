package com.matheusluizago.libraryapi.service;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void save(User user){
        var password = user.getPassword();
        user.setPassword(encoder.encode(password));
        repository.save(user);
    }

    public User getByLogin(String login){
        return repository.findByLogin(login);
    }
}

package com.matheusluizago.libraryapi.validator;

import com.matheusluizago.libraryapi.exceptions.DuplicateRegisterException;
import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {

    private final UserRepository repository;

    public UserValidator(UserRepository repository) {
        this.repository = repository;
    }

    public void validate(User user){
        if(loginAlreadyInUse(user.getLogin())){
            throw new DuplicateRegisterException("Login already in use.");
        }
    }

    private boolean loginAlreadyInUse(String login){
        Optional<User> foundUser = repository.findByLogin(login);

        return foundUser.isPresent();
    }
}

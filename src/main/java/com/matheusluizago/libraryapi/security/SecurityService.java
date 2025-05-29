package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityService {

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof CustomAuthentication customAuth){
            return customAuth.getUser();
        }

        return null;

        //Old version with userDetails
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String login = userDetails.getUsername();
//        return userService.getByLogin(login);

    }
}

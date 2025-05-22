package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userService.getByLogin(login);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        //Using user from userdetails
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.get().getLogin())
                .password(user.get().getPassword())
                .roles(user.get().getRoles().toArray(new String[user.get().getRoles().size()]))
                .build();
    }
}

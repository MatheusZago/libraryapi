package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomAuthentication implements Authentication {

    private final Optional<User> user;

    public CustomAuthentication(Optional<User> user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Transform roles in authorities
        return this.user.get()
                .getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
         //       .map(role -> new SimpleGrantedAuthority(role)) //The above code is this one but better
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return user.get().getLogin();
    }

    public Optional<User> getUser() {
        return user;
    }
}

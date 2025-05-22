package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider  implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public CustomAuthenticationProvider(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();

        Optional<User> foundUser = userService.getByLogin(login);

        if(foundUser.isEmpty()){
            throw new UsernameNotFoundException("Username and/or password incorrect");
        }

        String encryptedPassword = foundUser.get().getPassword();

        boolean passwordsCorrect = encoder.matches(inputPassword, encryptedPassword);

        if(passwordsCorrect){
            return new CustomAuthentication(foundUser.orElse(null));
        }

        throw new UsernameNotFoundException("Username and/or password incorrect");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //To see if the provider supports this kind of auth
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}

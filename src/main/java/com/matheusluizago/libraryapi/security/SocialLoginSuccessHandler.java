package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class SocialLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String DEFAULT_PASSWORD = "123";

    private final UserService userService;

    public SocialLoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email"); //getting the email from the Google token

        Optional<User> user = userService.getByEmail(email);

        if(user.isEmpty()){
            user = Optional.of(registerUserWithGoogle(email));
        }

        authentication = new CustomAuthentication(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private User registerUserWithGoogle(String email){
        User user;
        user = new User();
        user.setEmail(email);
        user.setLogin(createLoginByEmail(email));
        user.setPassword(DEFAULT_PASSWORD);
        user.setRoles(List.of("OPERATOR"));
        userService.save(user);
        return user;
    }

    private String createLoginByEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}

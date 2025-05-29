package com.matheusluizago.libraryapi.security;

import com.matheusluizago.libraryapi.model.User;
import com.matheusluizago.libraryapi.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    private UserService userService;

    public JwtCustomAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Only do this cusotmization if it is oauth2
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(shouldConvert(authentication)){
            String login = authentication.getName();
            Optional<User> user = userService.getByLogin(login);
            if(user != null){
                authentication = new CustomAuthentication(user);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response); //Pass the requisition forward

    }

    private boolean shouldConvert(Authentication authentication){
        return authentication != null && authentication instanceof JwtAuthenticationToken;
    }
}

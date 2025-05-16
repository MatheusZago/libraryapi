package com.matheusluizago.libraryapi.config;

import com.matheusluizago.libraryapi.security.CustomUserDetailsService;
import com.matheusluizago.libraryapi.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //Garantees that the right page is the one sending the request, in this case is off
                .formLogin(configurer -> {
                    configurer.loginPage("/login");
                })
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/users/**").permitAll();

                    authorize.requestMatchers(HttpMethod.POST, "/authors**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE, "/authors**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PUT, "/authors**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.GET, "/authors**").hasAnyRole("ADMIN", "USER");
                    authorize.requestMatchers("/books/**").hasAnyRole("USER", "ADMIN");
                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {

        return new CustomUserDetailsService(userService);

        //Creating in memory, above is the right way

//        UserDetails user1 = User.builder()
//                .username("user")
//                .password(encoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("admin")
//                .password(encoder.encode("321"))
//                .roles("ADMIN")
//                .build();

        //It is something that the class impelements
//        return new InMemoryUserDetailsManager(user1, user2);
    }
}

package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByLogin(String login);

    User findByEmail(String email);
}

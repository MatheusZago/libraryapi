package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);
}

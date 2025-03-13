package com.matheusluizago.libraryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<AuthorRepository, UUID> {
}

package com.matheusluizago.libraryapi.controller.dto;

import com.matheusluizago.libraryapi.model.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

//Record já é uma classe que é o próprio construtor
public record AuthorDTO(
        UUID id,
        @NotBlank(message = "Required field.")
        String name,
        @NotNull(message = "Required field.")
        LocalDate birthDate,
        @NotBlank(message = "Required field.")
        String nationality) {

    public Author mapToAuthor(){
        Author author = new Author();
        author.setName(name);
        author.setBirthdate(birthDate);
        author.setNationality(nationality);
        return author;
    }

}

package com.matheusluizago.libraryapi.controller.dto;

import com.matheusluizago.libraryapi.model.Author;

import java.time.LocalDate;
import java.util.UUID;

//Record já é uma classe que é o próprio construtor
public record AuthorDTO(
        UUID id,
        String name,
        LocalDate birthDate,
        String nationality) {

    public Author mapToAuthor(){
        Author author = new Author();
        author.setName(name);
        author.setBirthdate(birthDate);
        author.setNationality(nationality);
        return author;
    }

}

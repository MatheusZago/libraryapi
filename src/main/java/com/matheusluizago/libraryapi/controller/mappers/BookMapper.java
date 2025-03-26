package com.matheusluizago.libraryapi.controller.mappers;

import com.matheusluizago.libraryapi.controller.dto.RegisterBookDTO;
import com.matheusluizago.libraryapi.model.Book;
import com.matheusluizago.libraryapi.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    //Ta mapeando o valor author com uma expressão, pra definir author ele vai dar um findById
    @Mapping(target = "author", expression = "java( authorRepository.findById(dto.authorId()).orElse(null) )")
    @Mapping(target = "publicationDate", source = "publishDate")
    public abstract Book toEntity(RegisterBookDTO dto);


}

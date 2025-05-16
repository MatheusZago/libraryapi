package com.matheusluizago.libraryapi.controller.mappers;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "birthDate", target = "birthdate")
    Author toEntity(AuthorDTO dto);

    @Mapping(source = "birthdate", target = "birthDate")
    AuthorDTO toDTO(Author author);
}

package com.matheusluizago.libraryapi.controller.mappers;

import com.matheusluizago.libraryapi.controller.dto.AuthorDTO;
import com.matheusluizago.libraryapi.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") //transformando o mapper em um componente String
public interface AuthorMapper {

    @Mapping(source = "name", target = "name") //Usando se dois campos estão com nome diferntes
    Author toEntity(AuthorDTO dto);

    AuthorDTO toDTO(Author author);
}

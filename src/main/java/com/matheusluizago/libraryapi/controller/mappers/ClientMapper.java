package com.matheusluizago.libraryapi.controller.mappers;

import com.matheusluizago.libraryapi.controller.dto.ClientDTO;
import com.matheusluizago.libraryapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);
}

package com.matheusluizago.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

public record ErrorResponse(int status, String message, List<ErrorField> errors) {

    public static ErrorResponse defaultResponse(String message){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ErrorResponse conflict(String message){
        return new ErrorResponse(HttpStatus.CONFLICT.value(), message, List.of());
    }
}

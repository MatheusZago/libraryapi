package com.matheusluizago.libraryapi.exceptions;

public class DuplicateRegisterException extends RuntimeException {
    public DuplicateRegisterException(String message) {
        super(message);
    }
}

package com.example.cinema_api.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        //super hace referencia a la clase padre RuntimeException
        super(message);
    }
}

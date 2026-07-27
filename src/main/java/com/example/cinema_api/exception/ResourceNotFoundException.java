package com.example.cinema_api.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        //super hace referencia a runtimeexception
        super(message);
    }
}

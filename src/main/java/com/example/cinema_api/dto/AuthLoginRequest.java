package com.example.cinema_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthLoginRequest{
    @NotBlank
    @Email(message = "El email debe tener un formato valido")
    String email;
    @NotBlank
    String password;
}

package com.example.cinema_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthLoginRequest{
    @NotBlank
    @Email(message = "El email debe tener un formato valido")
    String email;
    @NotBlank
    String password;
}

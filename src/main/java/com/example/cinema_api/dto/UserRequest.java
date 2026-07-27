package com.example.cinema_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    @Schema(example = "prueba@gmail.com", description = "debe ser correo valido")
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Debe tener un formato valido")
    private String email;

    @Schema(example = "juanito123", description = "debe ser un username valido")
    @NotBlank(message = "El username no debe estar vacio")
    private String username;

    @Schema(example = "123456789", description = "una password de la")
    @NotBlank(message = "La password no debe ir vacia")
    @Size(min = 8, message = "la password debe ser mayor a 8 digitos")
    private String password;

    private Set<RoleIdRequest> roles;
}

package com.example.cinema_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// A diferencia de UserRequest, ningun campo es obligatorio: solo se valida el formato
// de los campos que efectivamente se envian, para permitir actualizaciones parciales.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPatchRequest {
    @Schema(example = "prueba@gmail.com", description = "debe ser correo valido si se envia")
    @Email(message = "Debe tener un formato valido")
    private String email;

    @Schema(example = "juanito123", description = "debe ser un username valido si se envia")
    private String username;

    @Schema(example = "123456789", description = "nueva password, se omite para no cambiarla")
    @Size(min = 8, message = "la password debe ser mayor a 8 digitos")
    private String password;

    private Set<RoleIdRequest> roles;
}

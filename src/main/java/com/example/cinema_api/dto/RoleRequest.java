package com.example.cinema_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleRequest {
    @NotBlank
    private String role;
    @NotEmpty(message = "Debe indicar al menos un permiso")
    private Set<PermissionIdRequest> permissions;
}

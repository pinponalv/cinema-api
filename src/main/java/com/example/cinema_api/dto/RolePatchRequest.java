package com.example.cinema_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// A diferencia de RoleRequest, ningun campo es obligatorio, para permitir
// actualizaciones parciales (solo se modifica lo que se envia).
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RolePatchRequest {
    private String role;
    private Set<PermissionIdRequest> permissions;
}

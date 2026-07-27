package com.example.cinema_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PermissionIdRequest {
    @NotNull(message = "El id del permiso es obligatorio")
    private Long id;
}

package com.example.cinema_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieRequest {
    @Schema(example = "El desguezador", description = "va el nombre de la pelicula")
    @NotBlank(message = "El titulo no puede estar vacio")
    private String title;

    @Schema(example = "Una historia basa en echos reales..", description = "de lo que trata la pelicula")
    @NotBlank(message = "La descripcion no puede estar vacia")
    private String description;

    @Schema(example = "accion, terror,aventuras, fantasias, ciencia ficcion", description = "categoria de la pelicula")
    @NotBlank(message = "La categoria no puede estar vacia")
    private String filmGenre;
}

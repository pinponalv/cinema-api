package com.example.cinema_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// A diferencia de MovieRequest, ningun campo es obligatorio, para permitir
// actualizaciones parciales (solo se modifica lo que se envia).
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoviePatchRequest {
    @Schema(example = "El desguezador", description = "va el nombre de la pelicula")
    private String title;

    @Schema(example = "Una historia basa en echos reales..", description = "de lo que trata la pelicula")
    private String description;

    @Schema(example = "accion, terror,aventuras, fantasias, ciencia ficcion", description = "categoria de la pelicula")
    private String filmGenre;

    private String posterUrl;
}

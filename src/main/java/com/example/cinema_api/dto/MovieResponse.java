package com.example.cinema_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private String filmGenre;
    private String posterUrl;
}

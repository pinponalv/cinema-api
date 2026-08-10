package com.example.cinema_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "movies")
public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String filmGenre;
    /**Esta la opcion de usar el genero de pelicula como una entidad aparte
     * pero para practicidad lo manejare como un string dentro de esta misma entidad**/
    @Column(nullable = true)
    private String posterUrl;
}

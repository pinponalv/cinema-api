package com.example.cinema_api.service;

import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMoviesService {
    MovieResponse createMovie(MovieRequest create);
    MovieResponse updateMovie(Long id, MovieRequest create);
    MovieResponse findMovieByTitle(String title);
    List<MovieResponse> findByFilmGenre(String filmGenre);
    Page<MovieResponse> findAllMovies(Pageable pageable);
    void deleteMovie(Long id);

}

package com.example.cinema_api.service;

import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;

import java.util.List;

public interface IMoviesService {
    MovieResponse createMovie(MovieRequest create);
    MovieResponse updateMovie(Long id, MovieRequest create);
    MovieResponse findMovieByTitle(String title);
    List<MovieResponse> findAllMovies();
    void deleteMovie(Long id);

}

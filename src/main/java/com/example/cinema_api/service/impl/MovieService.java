package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;
import com.example.cinema_api.entity.Movies;
import com.example.cinema_api.repository.MoviesRepository;
import com.example.cinema_api.service.IMoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService implements IMoviesService {

    private final MoviesRepository moviesRepository;

    @Override
    public MovieResponse createMovie(MovieRequest create) {
        Movies movies = new Movies();
        movies.setTitle(create.getTitle());
        movies.setDescription(create.getDescription());

        Movies saveMovie = moviesRepository.save(movies);
        return new MovieResponse(
                saveMovie.getId(),
                saveMovie.getTitle(),
                saveMovie.getDescription()
        );
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieRequest create) {
        Movies getMovie = moviesRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));

        if(create.getTitle() != null){
            getMovie.setTitle(create.getTitle());
        }
        if(create.getDescription() != null){
            getMovie.setDescription(create.getDescription());
        }

        Movies saveMovie = moviesRepository.save(getMovie);
        return new MovieResponse(
                saveMovie.getId(),
                saveMovie.getTitle(),
                saveMovie.getDescription()
        );
    }

    @Override
    public MovieResponse findMovieByTitle(String title) {
        Movies getMovieByTitle = moviesRepository.findMovieByTitle(title);
        return new MovieResponse(
                getMovieByTitle.getId(),
                getMovieByTitle.getTitle(),
                getMovieByTitle.getDescription()
        );
    }

    @Override
    public List<MovieResponse> findAllMovies() {
        List<Movies> getMovies = moviesRepository.findAll();
        List<MovieResponse> responseMovieDTOList = new ArrayList<>();
        for (Movies movies : getMovies) {
            MovieResponse responseMovieDTO = new MovieResponse(
                    movies.getId(),
                    movies.getTitle(),
                    movies.getDescription()
            );
            responseMovieDTOList.add(responseMovieDTO);
        }
        return responseMovieDTOList;
    }

    @Override
    public void deleteMovie(Long id) {
        if(!moviesRepository.existsById(id)){
            throw new RuntimeException("Pelicula no encontrada");
        }
        moviesRepository.deleteById(id);
    }
}

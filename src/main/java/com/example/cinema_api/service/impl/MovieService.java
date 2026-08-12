package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.MoviePatchRequest;
import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;
import com.example.cinema_api.entity.Movies;
import com.example.cinema_api.exception.InvalidRequestException;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.repository.MoviesRepository;
import com.example.cinema_api.service.IMoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService implements IMoviesService {

    private final MoviesRepository moviesRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public MovieResponse uploadPoster(Long id, MultipartFile file) {
        Movies movies = moviesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        String posterUrl = cloudinaryService.uploadImage(file);

        movies.setPosterUrl(posterUrl);
        Movies updated =  moviesRepository.save(movies);

        return new MovieResponse(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.getFilmGenre(),
                updated.getPosterUrl()
        );
    }

    @Override
    public MovieResponse createMovie(MovieRequest create) {
        Movies movies = new Movies();
        movies.setTitle(create.getTitle());
        movies.setDescription(create.getDescription());
        movies.setFilmGenre(create.getFilmGenre());

        Movies saveMovie = moviesRepository.save(movies);
        return new MovieResponse(
                saveMovie.getId(),
                saveMovie.getTitle(),
                saveMovie.getDescription(),
                saveMovie.getFilmGenre(),
                saveMovie.getPosterUrl()
        );
    }

    @Override
    public MovieResponse updateMovie(Long id, MoviePatchRequest create) {
        Movies getMovie = moviesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if(create.getTitle() != null){
            getMovie.setTitle(create.getTitle());
        }
        if(create.getDescription() != null){
            getMovie.setDescription(create.getDescription());
        }
        if(create.getFilmGenre() != null){
            getMovie.setFilmGenre(create.getFilmGenre());
        }

        Movies saveMovie = moviesRepository.save(getMovie);
        return new MovieResponse(
                saveMovie.getId(),
                saveMovie.getTitle(),
                saveMovie.getDescription(),
                saveMovie.getFilmGenre(),
                saveMovie.getPosterUrl()
        );
    }

    @Override
    public MovieResponse findMovieByTitle(String title) {
        Movies getMovieByTitle = moviesRepository.findMovieByTitle(title);

        if(getMovieByTitle == null){
            throw new ResourceNotFoundException("Movie not found");
        }

        return new MovieResponse(
                getMovieByTitle.getId(),
                getMovieByTitle.getTitle(),
                getMovieByTitle.getDescription(),
                getMovieByTitle.getFilmGenre(),
                getMovieByTitle.getPosterUrl()
        );
    }

    @Override
    public List<MovieResponse> findByFilmGenre(String filmGenre) {
        if(filmGenre.trim().isEmpty()){
            throw new  InvalidRequestException("Film Genre cannot be empty");
        }

        List<Movies> getMovies = moviesRepository.findByFilmGenre(filmGenre);

        if(getMovies.isEmpty()){
            throw new  ResourceNotFoundException("No movies found for genre " + filmGenre);
        }

        List<MovieResponse>  responseList = new ArrayList<>();
        for(Movies movies: getMovies){
            MovieResponse movieResponse = new MovieResponse(
                    movies.getId(),
                    movies.getTitle(),
                    movies.getDescription(),
                    movies.getFilmGenre(),
                    movies.getPosterUrl()
            );
            responseList.add(movieResponse);
        }

        return responseList;
    }

    // Pageable llega desde el controller (page, size, sort) e indica qué "porción" de resultados traer.
    @Override
    public Page<MovieResponse> findAllMovies(Pageable pageable) {
        // findAll(pageable) le pide a la base de datos solo esa porción (LIMIT/OFFSET), no toda la tabla.
        Page<Movies> moviesPage = moviesRepository.findAll(pageable);
        List<MovieResponse> responseMovieDTOList = new ArrayList<>();

        for (Movies movies : moviesPage.getContent()) {
            MovieResponse responseMovieDTO = new MovieResponse(
                    movies.getId(),
                    movies.getTitle(),
                    movies.getDescription(),
                    movies.getFilmGenre(),
                    movies.getPosterUrl()
            );
            responseMovieDTOList.add(responseMovieDTO);
        }
        return new PageImpl<>(responseMovieDTOList, pageable, moviesPage.getTotalElements());
    }

    @Override
    public void deleteMovie(Long id) {
        if(!moviesRepository.existsById(id)){
            throw new ResourceNotFoundException("Pelicula no encontrada");
        }
        moviesRepository.deleteById(id);
    }
}

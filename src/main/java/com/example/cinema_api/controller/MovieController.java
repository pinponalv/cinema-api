package com.example.cinema_api.controller;

import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;
import com.example.cinema_api.service.IMoviesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@Tag(name = "Movies", description = "Movies API Operations")
@RequiredArgsConstructor
public class MovieController {
    private final IMoviesService moviesService;

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest create){
        MovieResponse responseMovieDTO = moviesService.createMovie(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMovieDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @PatchMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @RequestBody MovieRequest create){
        MovieResponse responseMovieDTO = moviesService.updateMovie(id, create);
        return ResponseEntity.status(HttpStatus.OK).body(responseMovieDTO);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<MovieResponse> findMovieByTitle(@PathVariable String title){
        MovieResponse responseMovieDTO = moviesService.findMovieByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(responseMovieDTO);
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAllMovies(){
        List<MovieResponse> responseMovieDTO = moviesService.findAllMovies();
        return ResponseEntity.status(HttpStatus.OK).body(responseMovieDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MovieResponse> deleteMovie(@PathVariable Long id){
        moviesService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}

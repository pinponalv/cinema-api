package com.example.cinema_api.controller;

import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;
import com.example.cinema_api.service.IMoviesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movie")
@Tag(name = "Movies", description = "Movies API Operations")
@RequiredArgsConstructor
public class MovieController {
    private final IMoviesService moviesService;

    @Operation(summary = "Register movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest create){
        MovieResponse responseMovieDTO = moviesService.createMovie(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMovieDTO);
    }

    @Operation(summary = "Register movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @PatchMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest create){
        MovieResponse responseMovieDTO = moviesService.updateMovie(id, create);
        return ResponseEntity.status(HttpStatus.OK).body(responseMovieDTO);
    }

    @Operation(summary = "Find movies by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<MovieResponse> findMovieByTitle(@PathVariable String title){
        MovieResponse responseMovieDTO = moviesService.findMovieByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(responseMovieDTO);
    }


    @Operation(summary = "Find all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "204", description = "Not content"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    //GET /api/movie?page=0&size=10&sort=title,asc
    @GetMapping
    public ResponseEntity<Page<MovieResponse>> findAllMovies(
            @PageableDefault(size = 5, page = 0) Pageable pageable){
        Page<MovieResponse> responseMovieDTO = moviesService.findAllMovies(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseMovieDTO);
    }

    @Operation(summary = "Delete a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Not content"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        moviesService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}

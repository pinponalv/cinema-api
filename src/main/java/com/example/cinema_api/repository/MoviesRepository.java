package com.example.cinema_api.repository;

import com.example.cinema_api.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Long> {
    Movies findMovieByTitle(String title);
    List<Movies> findByFilmGenre(String filmGenre);
}

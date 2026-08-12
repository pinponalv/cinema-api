package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.MoviePatchRequest;
import com.example.cinema_api.dto.MovieRequest;
import com.example.cinema_api.dto.MovieResponse;
import com.example.cinema_api.entity.Movies;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.repository.MoviesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//Extender de esta clase nos evita tener que configurar codigo repetitivo
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    /**Con esta anotacion creamos un mock falso, es deecir, un falso repository, nos permite crear
     * pruebas sin una base de datos real,para que estas pruebas sean rapidas.
     * Controlamos el comportamiento con when(...).theReturn(...)
     * Verificamos que llamo con verify()
     * **/
    @Mock
    private MoviesRepository moviesRepository;

    /**Crea una instancia real del service, inyectamos los mocks que necesitamos
     * Es como decir a movieService inyectale el moviesrePository**/
    @InjectMocks
    private MovieService movieService;

    private Movies movie;
    private MovieRequest movieRequest;

    /**Es una anotacion de JUnit5 que se ejecuta antes de cada test para dar una pelicula fresca, para no generar
     * codigo repetitivo**/
    @BeforeEach
    void setUp() {
        movie = new Movies(1L, "Inception", "A mind-bending thriller","accion", null);
        movieRequest = new MovieRequest("Inception", "A mind-bending thriller", "accion", null);
    }

    @Test
    void createMovie_shouldReturnMovieResponse() {
        //cuando repository guarde alguna clase de movies luego regresa movie
        when(moviesRepository.save(any(Movies.class))).thenReturn(movie);

        //llamo a crear movie y le paso la pelicula
        MovieResponse response = movieService.createMovie(movieRequest);

        //afirma que el response no es nulo
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Inception");
        assertThat(response.getDescription()).isEqualTo("A mind-bending thriller");
        //verifica que el repository llamada almenos una vez al metodo save con cualquier objeto de movies
        verify(moviesRepository, times(1)).save(any(Movies.class));
    }

    //actualizamos solo el campo que le porporcionamos
    @Test
    void updateMovie() {
        MoviePatchRequest updateRequest = new MoviePatchRequest("Inception 2", null, null, null);
        when(moviesRepository.findById(1L)).thenReturn(Optional.of(movie));
        //cuando guarde alguna clase de movie me va retornar la pelicula
        when(moviesRepository.save(any(Movies.class))).thenReturn(movie);

        //creo una response que guardara la pelicula actualizada,le paso el id y el request
        MovieResponse response = movieService.updateMovie(1L, updateRequest);

        //afirmamos que si los titulos y descripcion son iguales
        assertThat(response.getTitle()).isEqualTo("Inception 2");
        assertThat(response.getDescription()).isEqualTo("A mind-bending thriller");

        //verifica que el repository guarde la movie
        verify(moviesRepository).save(movie);
    }

    //la actualizacion de la pelicula deberia lanzar una excepcion cuando no se encuentre la pelicula
    @Test
    void updateMovie_throwExceptionWhenMovieNotFound() {
        when(moviesRepository.findById(99L)).thenReturn(Optional.empty());

        MoviePatchRequest patchRequest = new MoviePatchRequest("Inception", "A mind-bending thriller", "accion", null);
        //afirma que fue lanzado por
        assertThatThrownBy(() -> movieService.updateMovie(99L, patchRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Movie not found");

        //verifica que el repositorio no llame nunca al metodo save con ningun objeto
        verify(moviesRepository, never()).save(any());
    }

    @Test
    void findMovieByTitle() {
        when(moviesRepository.findMovieByTitle("Inception")).thenReturn(movie);

        MovieResponse response = movieService.findMovieByTitle("Inception");

        assertThat(response.getTitle()).isEqualTo("Inception");
        verify(moviesRepository).findMovieByTitle("Inception");
    }

    @Test
    void findByFilmGenre() {
        List<Movies>  moviesList = new ArrayList<>(List.of(movie));
        when(moviesRepository.findByFilmGenre("accion")).thenReturn(moviesList);

        List<MovieResponse> movieResponseList = movieService.findByFilmGenre("accion");

        assertThat(movieResponseList).isNotNull();
        assertThat(movieResponseList.get(0).getFilmGenre()).isEqualTo("accion");
        verify(moviesRepository).findByFilmGenre("accion");

    }

    @Test
    void findByFilmGenre_throwExceptionWhenGenreNotFound() {
        when(moviesRepository.findByFilmGenre("aventura")).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> movieService.findByFilmGenre("aventura"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No movies found for genre aventura");
    }


    //la funcion para buscar todas las peliculas debera devolver una pagina de respuesta con las peliculas
    @Test
    void findAllMovies() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Movies> moviesPage = new PageImpl<>(List.of(movie), pageable, 1);
        when(moviesRepository.findAll(pageable)).thenReturn(moviesPage);

        var result = movieService.findAllMovies(pageable);

        assertThat(result.getContent()).hasSize(1);
        //obtengo el elemento cero de una lista
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Inception");
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void deleteMovie() {
        when(moviesRepository.existsById(1L)).thenReturn(true);

        movieService.deleteMovie(1L);

        //verifica que el repositorio llame una vez al metodo delete by id con el id 1L
        verify(moviesRepository, times(1)).deleteById(1L);
    }

    //lanza una exception cuando la pelicula no existe
    @Test
    void deleteMovie_throwExceptionWhenMovieDoesNotExist() {
        when(moviesRepository.existsById(99L)).thenReturn(false);

        //afirma que fue lanzado por
        assertThatThrownBy(() -> movieService.deleteMovie(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pelicula no encontrada");

        //verifica que el repositorio nunca llame al deletebyid con algun long
        verify(moviesRepository, never()).deleteById(anyLong());
    }
}

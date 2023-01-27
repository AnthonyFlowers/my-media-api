package mymedia.domain;

import mymedia.data.MovieRepository;
import mymedia.models.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovieServiceTest {
    @MockBean
    MovieRepository repository;
    @Autowired
    MovieService service;

    @Test
    void shouldFindAllMovies() {
        Page<Movie> movies = getMovieList();
        when(repository.findAll(any(PageRequest.class))).thenReturn(movies);
        Page<Movie> allMovies = service.findMovies(0, 1);
        assertEquals(movies, allMovies);
    }

    @Test
    void shouldFindByMovieName() {
        Movie movie = new Movie();
        when(repository.findFirstByMovieName(anyString())).thenReturn(movie);
        Movie foundMovie = service.findByMovieName("Test");
        assertNotNull(foundMovie);
        assertEquals(movie, foundMovie);
    }

    @Test
    void shouldNotFindByMovieName() {
        when(repository.findFirstByMovieName(anyString())).thenReturn(null);
        Movie foundMovie = service.findByMovieName("shouldn't find");
        assertNull(foundMovie);
    }

    @Test
    void shouldNotFindBlankMovieName() {
        Movie foundMovie = service.findByMovieName("");
        assertNull(foundMovie);
    }

    @Test
    void shouldNotFindEmptyMovieName() {
        Movie foundMovie = service.findByMovieName("   ");
        assertNull(foundMovie);
    }

    private static Page<Movie> getMovieList() {
        Movie ironMan = new Movie();
        ironMan.setMovieId(1);
        ironMan.setMovieName("Iron Man");
        ironMan.setMovieYear(2008);
        ironMan.setMovieLength(128);
        Movie inception = new Movie();
        inception.setMovieId(2);
        inception.setMovieName("Inception");
        inception.setMovieYear(2010);
        inception.setMovieLength(148);
        return new PageImpl<>(List.of(ironMan, inception));
    }

}
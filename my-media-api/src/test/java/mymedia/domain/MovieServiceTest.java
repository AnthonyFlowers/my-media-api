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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
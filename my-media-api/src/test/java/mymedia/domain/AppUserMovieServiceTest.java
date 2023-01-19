package mymedia.domain;

import mymedia.data.AppUserMovieRepository;
import mymedia.models.AppUserMovie;
import mymedia.models.Movie;
import mymedia.security.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserMovieServiceTest {

    @MockBean
    AppUserMovieRepository repository;
    @MockBean
    MovieService movieService;

    @Autowired
    AppUserMovieService service;

    @Test
    void shouldFindByUserMovieIdAndUser() {
        AppUserMovie expected = new AppUserMovie();
        when(repository.findByAppUserMovieIdAndUser(anyInt(), any(AppUser.class)))
                .thenReturn(expected);
        AppUserMovie actual = service.findByUserMovieIdAndUser(1, new AppUser());
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByUserMovieId() {
        AppUserMovie expected = new AppUserMovie();
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(expected));
        AppUserMovie actual = repository.findById(1).orElse(null);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindByUserMovieId() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        AppUserMovie actual = repository.findById(1).orElse(null);
        assertNull(actual);
    }

    @Test
    void shouldFindAppUserMovies() {
        AppUser user = new AppUser();
        user.setUsername("johnsmith");
        Page<AppUserMovie> expected = Page.empty();
        when(repository.findByUserUsername(any(Pageable.class), anyString()))
                .thenReturn(expected);
        Page<AppUserMovie> actual = service.findUserMovies(1, 1, user);
        assertEquals(actual, expected);
    }

    @Test
    void shouldCreateAppUserMovie() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        Movie movie = new Movie();
        movie.setMovieId(100);
        AppUserMovie expected = new AppUserMovie();
        expected.setUser(user);
        expected.setMovie(movie);
        when(repository.save(any(AppUserMovie.class)))
                .thenReturn(expected);
        when(movieService.findById(anyInt()))
                .thenReturn(movie);
        Result<AppUserMovie> result = service.create(user, movie);
        assertTrue(result.isSuccess());
        AppUserMovie actual = result.getPayload();
        assertEquals(user.getAppUserId(), actual.getUserId());
        assertEquals(movie.getMovieId(), actual.getMovie().getMovieId());
    }

    @Test
    void shouldUpdateUserMovie() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        Movie movie = new Movie();
        movie.setMovieId(300);
        AppUserMovie expected = new AppUserMovie();
        expected.setUser(user);
        expected.setMovie(movie);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(expected));
        when(repository.save(any(AppUserMovie.class)))
                .thenReturn(expected);
        Result<AppUserMovie> result = service.update(expected, user);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        AppUserMovie actual = result.getPayload();
        assertEquals(user.getAppUserId(), actual.getUserId());
    }

    @Test
    void shouldNotUpdateUserMovieNotFound() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        Movie movie = new Movie();
        movie.setMovieId(300);
        AppUserMovie expected = new AppUserMovie();
        expected.setUser(user);
        expected.setMovie(movie);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Result<AppUserMovie> result = service.update(expected, user);
        assertFalse(result.isSuccess());
        List<String> messages = result.getMessages();
        assertEquals("Could not find that movie entry to update", messages.get(0));
    }

    @Test
    void shouldNotUpdateUserMovieWrongUser() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        AppUser badUser = new AppUser();
        badUser.setAppUserId(9001);
        Movie movie = new Movie();
        movie.setMovieId(300);
        AppUserMovie expected = new AppUserMovie();
        expected.setUser(user);
        expected.setMovie(movie);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(expected));
        Result<AppUserMovie> result = service.update(expected, badUser);
        assertFalse(result.isSuccess());
        List<String> messages = result.getMessages();
        assertEquals("Could not update that movie entry", messages.get(0));
    }

    @Test
    void shouldDeleteAppUserMovie() {
        AppUser user = new AppUser();
        user.setAppUserId(2);
        AppUserMovie expected = new AppUserMovie();
        expected.setAppUserMovieId(3);
        expected.setUser(user);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(expected));
        Result<?> result = service.delete(expected, user);
        assertTrue(result.isSuccess());
        assertEquals(0, result.getMessages().size());
    }

    @Test
    void shouldNotDeleteAppUserMovie() {
        AppUser user = new AppUser();
        user.setAppUserId(2);
        AppUserMovie expected = new AppUserMovie();
        expected.setAppUserMovieId(3);
        expected.setUser(user);
        AppUser badUser = new AppUser();
        badUser.setAppUserId(3);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(expected));
        Result<?> result = service.delete(expected, badUser);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }
}
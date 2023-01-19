package mymedia.data;

import mymedia.models.AppUserMovie;
import mymedia.models.Movie;
import mymedia.security.AppUser;
import mymedia.security.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserMovieRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    AppUserMovieRepository userMovieRepository;
    @Autowired
    AppUserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;

    private boolean isSetUp = false;

    @BeforeEach
    public void setUp() {
        if (!isSetUp) {
            KnownGoodState.setKnownGoodState(jdbcTemplate);
            isSetUp = true;
        }
    }

    @Test
    void shouldGetAtLeast2MoviesForUserJohnsmith() {
        Page<AppUserMovie> appUserMovies = userMovieRepository.findByUserUsername(
                Pageable.ofSize(10),
                "johnsmith"
        );
        assertTrue(2 <= appUserMovies.getNumberOfElements());
    }

    @Test
    void shouldGet1MovieForJohnsmithWithUserData() {
        Page<AppUserMovie> appUserMovies = userMovieRepository.findByUserUsername(
                Pageable.ofSize(1),
                "johnsmith"
        );
        List<AppUserMovie> appUserMoviesContent = appUserMovies.getContent();
        assertEquals(1, appUserMoviesContent.size());
        AppUserMovie appUserMovie = appUserMoviesContent.get(0);
        assertFalse(appUserMovie.isWatched());
    }

    @Test
    void shouldCreateAppUserMovie() {
        AppUser user = userRepository.findByUsername("janedoe");
        Movie movie = movieRepository.findById(2).orElse(null);
        assertNotNull(user);
        assertNotNull(movie);
        AppUserMovie userMovie = new AppUserMovie();
        userMovie.setUser(user);
        userMovie.setMovie(movie);
        AppUserMovie savedUserMovie = userMovieRepository.save(userMovie);
        assertEquals(4, savedUserMovie.getAppUserMovieId());
    }

    @Test
    void shouldNotCreateAppUserMovieDuplicate() {
        AppUser user = userRepository.findByUsername("johnsmith");
        Movie movie = movieRepository.findById(1).orElse(null);
        assertNotNull(user);
        assertNotNull(movie);
        AppUserMovie userMovie = new AppUserMovie();
        userMovie.setUser(user);
        userMovie.setMovie(movie);
        assertThrows(DataIntegrityViolationException.class, () -> {
            userMovieRepository.save(userMovie);
        });
    }

    @Test
    void shouldUpdateAppUserMovieWatchedStatus() {
        AppUserMovie userMovie = userMovieRepository.findByUserUsername(
                Pageable.ofSize(1), "johnsmith").getContent().get(0);
        userMovie.setWatched(true);
        AppUserMovie savedMovie = userMovieRepository.save(userMovie);
        assertTrue(savedMovie.isWatched());
    }

    @Test
    void shouldDeleteAppUserMovie() {
        List<AppUserMovie> userMovies = userMovieRepository.findByUserUsername(
                Pageable.unpaged(),
                "janedoe").getContent();
        assertFalse(userMovies.isEmpty());
        AppUserMovie userMovie = userMovies.get(0);
        userMovieRepository.delete(userMovie);
    }

    @Test
    void shouldIncrementAppUserMovieWatchCount() {
        AppUserMovie userMovie = userMovieRepository
                .findByUserUsername(Pageable.ofSize(1), "johnsmith")
                .getContent().get(0);
        int watchCount = userMovie.getWatchCount();
        userMovie.setWatchCount(watchCount + 1);
        AppUserMovie savedMovie = userMovieRepository.save(userMovie);
        assertEquals(watchCount + 1, savedMovie.getWatchCount());
    }

    @Test
    void shouldDecrementAppUserMovieWatchCount() {
        AppUserMovie userMovie = userMovieRepository
                .findByUserUsername(Pageable.ofSize(1), "johnsmith")
                .getContent().get(0);
        int watchCount = userMovie.getWatchCount();
        userMovie.setWatchCount(watchCount - 1);
        AppUserMovie savedMovie = userMovieRepository.save(userMovie);
        assertEquals(watchCount - 1, savedMovie.getWatchCount());
    }

}
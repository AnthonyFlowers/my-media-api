package mymedia.data;

import mymedia.models.Movie;
import mymedia.models.MovieNightAppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieNightAppUserRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MovieNightAppUserRepository repository;

    private boolean isSetUp = false;

    @BeforeEach
    public void setUp() {
        if (!isSetUp) {
            KnownGoodState.setKnownGoodState(jdbcTemplate);
            isSetUp = true;
        }
    }

    @Test
    void createAppUserGroupMovie() {
        MovieNightAppUser newUserGroupMovie = new MovieNightAppUser();
        newUserGroupMovie.setAppUserId(1);
        newUserGroupMovie.setGroupId(4);
        newUserGroupMovie.setIsModerator(false);
        MovieNightAppUser saved = repository.save(newUserGroupMovie);
        assertTrue(saved.getAppUserGroupId() > 0);
    }

    @Test
    void findAppUserGroupMovieByUserId() {
        List<MovieNightAppUser> userGroupMovies = repository.findByAppUserId(1);
        assertTrue(userGroupMovies.size() > 0);
    }

    @Test
    void findAppUserGroupMoviesByGroupId() {
        List<MovieNightAppUser> appUserMovieNightGroupMovies = repository.findByGroupId(1);
        assertTrue(appUserMovieNightGroupMovies.size() > 1);
    }

    @Test
    void updateAppUserGroupMovie() {
        MovieNightAppUser appUserMovieNightGroupMovie = repository.findById(2).orElse(null);
        assertNotNull(appUserMovieNightGroupMovie);
        Movie movie = new Movie();
        movie.setMovieId(1);
        appUserMovieNightGroupMovie.setUserMovieVote(movie);
        MovieNightAppUser saved = repository.save(appUserMovieNightGroupMovie);
        assertNotNull(saved.getUserMovieVote());
    }

    @Test
    void deleteAppUserGroupMovie() {
        MovieNightAppUser appUserMovieNightGroupMovie = repository.findById(3).orElse(null);
        assertNotNull(appUserMovieNightGroupMovie);
        repository.delete(appUserMovieNightGroupMovie);
        assertNull(repository.findById(3).orElse(null));
    }

}
package mymedia.domain;

import mymedia.data.KnownGoodState;
import mymedia.data.MovieNightAppUserRepository;
import mymedia.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MovieNightAppUserServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Mock
    MovieNightAppUserRepository repository;
    @Autowired
    MovieNightAppUserService service;

    private boolean isSetUp = false;

    @BeforeEach
    public void setUp() {
        if (!isSetUp) {
            KnownGoodState.setKnownGoodState(jdbcTemplate);
            isSetUp = true;
        }
    }

    @Test
    void getUserVotes() {
        Movie topMovie = service.getTopMovie(1);
        assertNotNull(topMovie);
    }
}
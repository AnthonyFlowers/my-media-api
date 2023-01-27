package mymedia.domain;

import mymedia.data.MovieNightAppUserRepository;
import mymedia.models.MovieNightAppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovieNightAppUserServiceTest {


    @MockBean
    MovieNightAppUserRepository repository;
    @Autowired
    MovieNightAppUserService service;


    @Test
    void shouldFindMovieNightAppUser() {
        when(repository.findByAppUserId(1)).thenReturn(List.of(new MovieNightAppUser()));
        List<MovieNightAppUser> users = service.findByAppUserId(1);
        assertTrue(users.size() > 0);
    }
}
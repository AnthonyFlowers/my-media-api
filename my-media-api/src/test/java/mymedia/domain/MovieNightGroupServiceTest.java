package mymedia.domain;

import mymedia.data.GroupWithTopMovie;
import mymedia.data.MovieNightGroupRepository;
import mymedia.models.Movie;
import mymedia.models.MovieNightGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovieNightGroupServiceTest {

    @MockBean
    MovieNightGroupRepository repository;
    @Autowired
    MovieNightGroupService service;


    @Test
    void shouldFindGroupsWithTopMovies() {
        when(repository.findGroupsWithTopMovies()).thenReturn(getGroupWithTopMovies());
        GroupWithTopMovie groupsWithTopMovies = service.findGroupsWithTopMovies().get(0);
        assertNotNull(groupsWithTopMovies);
        assertEquals(78, groupsWithTopMovies.getTopMovie().getMovieId());
        assertEquals("Test Group", groupsWithTopMovies.getGroup().getGroupName());
    }

    private List<GroupWithTopMovie> getGroupWithTopMovies() {
        return List.of(new GroupWithTopMovie() {
            @Override
            public MovieNightGroup getGroup() {
                return testGroup();
            }

            @Override
            public Movie getTopMovie() {
                return testMovie();
            }
        });
    }

    private MovieNightGroup testGroup() {
        MovieNightGroup group = new MovieNightGroup();
        group.setGroupId(1);
        group.setMovies(List.of(testMovie()));
        group.setGroupName("Test Group");
        return group;
    }

    private Movie testMovie() {
        Movie testMovie = new Movie();
        testMovie.setMovieId(78);
        testMovie.setMovieName("Test Movie");
        return testMovie;
    }

}
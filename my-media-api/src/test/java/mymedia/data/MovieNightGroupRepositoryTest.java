package mymedia.data;

import mymedia.models.MovieNightGroup;
import mymedia.security.AppUser;
import mymedia.security.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieNightGroupRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MovieNightGroupRepository repository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    AppUserRepository userRepository;

    private boolean isSetUp = false;
    @BeforeEach
    public void setUp() {
        if (!isSetUp) {
            KnownGoodState.setKnownGoodState(jdbcTemplate);
            isSetUp = true;
        }
    }

    @Test
    void shouldCreateNewGroup() {
        MovieNightGroup newGroup = new MovieNightGroup();
        newGroup.setGroupName("New Group");
        newGroup.setMovies(movieRepository.findAll());
        MovieNightGroup savedGroup = repository.save(newGroup);
        assertTrue(savedGroup.getGroupId() > 0);
        assertNotNull(savedGroup.getMovies());
    }

    @Test
    void shouldFindMoreThan2Groups() {
        List<MovieNightGroup> groups = repository.findAll();
        assertTrue(groups.size() > 1);
    }

    @Test
    @Transactional
    void shouldFindUserInTestGroup() {
        MovieNightGroup group = repository.findById(1).orElse(null);
        assertNotNull(group);
        assertNotNull(group.getUsers());
        assertEquals(2, group.getUsers().size());
    }

    @Test
    @Transactional
    void shouldAddUserToFriendsGroup() {
        MovieNightGroup group = repository.findById(2).orElse(null);
        AppUser userToAdd = userRepository.findById(2).orElse(null);
        assertNotNull(userToAdd);
        assertNotNull(group);
        group.addUser(userToAdd);
        MovieNightGroup savedGroup = repository.save(group);
        assertTrue(savedGroup.getUsers().size() >= 2);
    }

    @Test
    void shouldFindTestGroupById() {
        MovieNightGroup group = repository.findById(1).orElse(null);
        assertNotNull(group);
        assertEquals("Test Group", group.getGroupName());
    }

    @Test
    void shouldUpdateFriendsGroupName() {
        MovieNightGroup group = repository.findById(2).orElse(null);
        assertNotNull(group);
        group.setGroupName("Discord Friends");
        MovieNightGroup updatedGroup = repository.save(group);
        assertEquals("Discord Friends", updatedGroup.getGroupName());
    }

    @Test
    void shouldDeleteOnlineGroup() {
        MovieNightGroup group = repository.findById(3).orElse(null);
        assertNotNull(group);
        repository.delete(group);
        assertNull(repository.findById(3).orElse(null));
    }
}
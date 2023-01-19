package mymedia.security;

import mymedia.data.KnownGoodState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private boolean isSetUp = false;

    @BeforeEach
    public void setUp() {
        if (!isSetUp) {
            KnownGoodState.setKnownGoodState(jdbcTemplate);
            isSetUp = true;
        }
    }

    @Test
    void shouldCreateNewUser() {
        AppUser user = new AppUser();
        user.setUsername("newuser");
        user.setPassword("password_hash");
        user.setEnabled(true);

        AppUser savedUser = repository.save(user);
        assertEquals(4, savedUser.getAppUserId());
    }

    @Test
    void shouldFindUserJohnSmith() {
        AppUser user = repository.findByUsername("johnsmith");
        assertNotNull(user);
        assertEquals("johnsmith", user.getUsername());
    }

    @Test
    void shouldUpdateUserJaneDoe() {
        AppUser user = repository.findByUsername("janedoe");
        assertNotNull(user);
        user.setUsername("jane_doe");
        repository.save(user);

        AppUser updated = repository.findByUsername("jane_doe");
        assertNotNull(updated);
        assertEquals("jane_doe", updated.getUsername());
    }

    @Test
    void shouldNotFindUserThatDoesNotExist() {
        AppUser user = repository.findByUsername("doesnotexist");
        assertNull(user);
    }

    @Test
    void shouldDeleteUserAshKetchum() {
        AppUser user = repository.findByUsername("ashketchum");
        assertNotNull(user);
        repository.delete(user);
        AppUser rm = repository.findByUsername("ashketchum");
        assertNull(rm);
    }

    @Test
    @Transactional
    void shouldFindAdminAuthorityForJohnSmith() {
        AppUser user = repository.findByUsername("johnsmith");
        assertNotNull(user);
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch((a) -> Objects.equals(a.getAuthority(), "ADMIN")));
    }
}
package mymedia.security;

import mymedia.domain.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserServiceTest {

    @MockBean
    AppUserRepository repository;
    @Autowired
    AppUserService service;

    @Test
    void shouldFindUser() {
        AppUser user = getTestUser();
        when(repository.findByUsername(anyString()))
                .thenReturn(user);
        UserDetails userDetails = service.loadUserByUsername("johnsmith");
        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    void shouldFindUserByUsername() {
        AppUser user = getTestUser();
        when(repository.findByUsername(user.getUsername()))
                .thenReturn(user);
        assertTrue(service.usernameExists("johnsmith"));
    }

    @Test
    void shouldNotFindUserByUsername() {
        when(repository.findByUsername(anyString())).thenReturn(null);
        assertFalse(service.usernameExists("random_user"));
    }

    @Test
    void shouldCreateUser() {
        AppUser user = getTestUser();
        when(repository.save(any(AppUser.class)))
                .thenReturn(user);
        Result<AppUser> result = service.create("johnsmith", "P@ssw0rd");
        assertTrue(result.isSuccess());
        assertEquals(user, result.getPayload());
    }

    @Test
    void shouldNotCreateUserInvalidPassword() {
        Result<AppUser> result = service.create("johnsmith", "password");
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotCreateUserUsernameInUse() {
        when(repository.findByUsername(anyString()))
                .thenReturn(new AppUser());
        Result<AppUser> result = service.create("johnsmith", "password12!");
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldUpdateUser() {
        AppUser user = getTestUser();
        Result<AppUser> result = service.update(user);
        assertEquals(user, result.getPayload());
    }

    @Test
    void shouldNotUpdateUser() {
        AppUser user = getTestUser();
        doThrow(new DuplicateKeyException(""))
                .when(repository).save(user);
        Result<AppUser> result = service.update(user);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    private AppUser getTestUser() {
        return new AppUser(1, "johnsmith",
                "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                true);
    }
}
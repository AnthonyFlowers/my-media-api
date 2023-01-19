package mymedia.domain;

import mymedia.data.AppUserTvShowRepository;
import mymedia.models.AppUserTvShow;
import mymedia.models.TvShow;
import mymedia.security.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserTvShowServiceTest {

    @MockBean
    AppUserTvShowRepository repository;
    @MockBean
    TvShowService tvShowService;

    @Autowired
    AppUserTvShowService service;

    @Test
    void shouldCreateUserTvShow() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        TvShow show = new TvShow();
        show.setTvShowId(1);
        when(tvShowService.findById(1)).thenReturn(show);
        Result<AppUserTvShow> result = service.create(show, user);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotCreateUserTvShowNotFound() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        TvShow show = new TvShow();
        show.setTvShowId(1);
        when(tvShowService.findById(1)).thenReturn(null);
        Result<AppUserTvShow> result = service.create(show, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Could not find that TV Show"));
    }

    @Test
    void shouldNotCreateUserTvShowNull() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        Result<AppUserTvShow> result = service.create(null, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Cannot add an entry with a null TV Show"));
    }

    @Test
    void shouldNotCreateUserTvShowAlreadyExists() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        when(repository.findByUserAppUserIdAndTvShowTvShowId(anyInt(), anyInt()))
                .thenReturn(new AppUserTvShow());
        Result<AppUserTvShow> result = service.create(new TvShow(), user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("User already has an entry for that TV Show"));
    }

    @Test
    void shouldDeleteUserTvShow() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        userTvShow.setAppUserTvShowId(1);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(userTvShow));
        Result<?> result = service.delete(userTvShow, user);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeleteUserTvShowNotFound() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        userTvShow.setAppUserTvShowId(1);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Result<?> result = service.delete(userTvShow, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Could not find that tv show entry"));
    }

    @Test
    void shouldNotDeleteUserTvShowWrongUser() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        AppUser wrongUser = new AppUser();
        wrongUser.setAppUserId(2);
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        userTvShow.setAppUserTvShowId(1);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(userTvShow));
        Result<?> result = service.delete(userTvShow, wrongUser);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Could not delete that tv show entry"));
    }

    @Test
    void shouldUpdateUserTvShow() {
        AppUser user = new AppUser();
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(userTvShow));
        when(repository.save(any()))
                .thenReturn(userTvShow);
        Result<AppUserTvShow> result = service.update(userTvShow, user);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
    }

    @Test
    void shouldNotUpdateUserTvShowNotFound() {
        AppUser user = new AppUser();
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Result<AppUserTvShow> result = service.update(userTvShow, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Could not find that tv show entry to update"));
    }

    @Test
    void shouldNotUpdateUserTvShowWrongUser() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        AppUser wrongUser = new AppUser();
        wrongUser.setAppUserId(2);
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(userTvShow));
        Result<AppUserTvShow> result = service.update(userTvShow, wrongUser);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Could not update that tv show entry"));
    }

    @Test
    void shouldNotUpdateUserTvShowInvalidSeason() {
        AppUser user = new AppUser();
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        userTvShow.setSeason(-1);
        Result<AppUserTvShow> result = service.update(userTvShow, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Minimum season number can not be less than 0"));
    }

    @Test
    void shouldNotUpdateUserTvShowInvalidEpisode() {
        AppUser user = new AppUser();
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        userTvShow.setEpisode(-1);
        Result<AppUserTvShow> result = service.update(userTvShow, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Minimum episode number can not be less than 0"));
    }

    @Test
    void shouldNotUpdateUserTvShowInvalidWatchCount() {
        AppUser user = new AppUser();
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(user);
        userTvShow.setWatchCount(-1);
        Result<AppUserTvShow> result = service.update(userTvShow, user);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Minimum watch count can not be less than 0"));
    }
}

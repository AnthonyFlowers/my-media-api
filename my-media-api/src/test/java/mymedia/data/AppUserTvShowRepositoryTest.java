package mymedia.data;

import mymedia.models.AppUserTvShow;
import mymedia.models.TvShow;
import mymedia.security.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserTvShowRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    AppUserTvShowRepository userTvShowRepository;
    @Autowired
    TvShowRepository showRepository;
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
    void shouldAddSharkTankToJohnSmith() {
        AppUserTvShow userTvShow = new AppUserTvShow();
        userTvShow.setUser(KnownGoodState.getJohnSmith(userRepository));
        TvShow sharkTank = showRepository.findById(4).orElse(null);
        assertNotNull(sharkTank);
        userTvShow.setTvShow(sharkTank);
        userTvShow.setSeason(1);
        userTvShow.setEpisode(1);

        AppUserTvShow savedUserTvShow = userTvShowRepository.save(userTvShow);
        assertEquals(4, savedUserTvShow.getAppUserTvShowId());
    }

    @Test
    void shouldFindRickAndMortyInJohnSmithsShows() {
        AppUserTvShow userTvShow = userTvShowRepository.findByUserUsername(
                Pageable.unpaged(),
                "johnsmith"
        ).getContent().get(0);
        assertNotNull(userTvShow);
        assertEquals("Rick and Morty", userTvShow.getTvShow().getTvShowName());
    }

    @Test
    void shouldUpdateJohnSmithsShow(){
        AppUserTvShow userTvShow = userTvShowRepository.findByUserUsername(
                Pageable.unpaged(),
                "johnsmith"
        ).getContent().get(1);
        assertNotNull(userTvShow);
        userTvShow.setSeason(2);
        AppUserTvShow savedShow = userTvShowRepository.save(userTvShow);
        assertEquals(2, savedShow.getSeason());
    }

    @Test
    void shouldDeleteJaneDoesShow() {
        AppUserTvShow showToDelete = userTvShowRepository.findByUserUsername(
                Pageable.unpaged(), "janedoe"
        ).getContent().get(0);
        assertNotNull(showToDelete);
        userTvShowRepository.delete(showToDelete);
        assertEquals(0, userTvShowRepository.findByUserUsername(
                Pageable.unpaged(), "janedoe"
        ).getContent().size());
    }
}
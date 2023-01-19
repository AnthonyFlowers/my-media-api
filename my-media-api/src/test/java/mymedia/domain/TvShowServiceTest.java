package mymedia.domain;

import mymedia.data.TvShowRepository;
import mymedia.models.TvShow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TvShowServiceTest {

    @MockBean
    TvShowRepository repository;
    @Autowired
    TvShowService service;

    @Test
    void shouldFindTvShows() {
        Page<TvShow> expected = getTvShows();
        when(repository.findAll(any(PageRequest.class)))
                .thenReturn(expected);
        Page<TvShow> actual = service.findTvShows(1, 50);
        assertEquals(expected.getSize(), actual.getSize());
    }

    private Page<TvShow> getTvShows() {
        TvShow rickAndMorty = new TvShow();
        rickAndMorty.setTvShowName("Rick and Morty");
        TvShow dragonBallSuper = new TvShow();
        dragonBallSuper.setTvShowName("Dragon Ball Super");
        return new PageImpl<>(List.of(rickAndMorty, dragonBallSuper));
    }
}
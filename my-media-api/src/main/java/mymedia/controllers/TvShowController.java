package mymedia.controllers;

import mymedia.domain.Result;
import mymedia.domain.TvShowService;
import mymedia.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api/tv-show")
public class TvShowController {

    private final TvShowService service;

    public TvShowController(TvShowService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getTvShows(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int pageSize) {
        Page<TvShow> tvShows = service.findTvShows(page, pageSize);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
                .body(tvShows);
    }

    @GetMapping("/search")
    public ResponseEntity<?> tvShowSearch(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int pageSize,
            @RequestParam(required = false) String title) {
        Result<Page<TvShow>> result = service.search(page, pageSize, title);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
}

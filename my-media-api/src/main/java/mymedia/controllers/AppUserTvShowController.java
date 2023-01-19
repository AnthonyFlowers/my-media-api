package mymedia.controllers;

import mymedia.domain.AppUserTvShowService;
import mymedia.domain.Result;
import mymedia.models.AppUserTvShow;
import mymedia.models.TvShow;
import mymedia.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user/tv-show")
public class AppUserTvShowController {

    private final AppUserTvShowService service;

    public AppUserTvShowController(AppUserTvShowService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAppUserTvShows(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Page<AppUserTvShow> userTvShows = service.findUserTvShows(page, pageSize, appUser);
        return new ResponseEntity<>(userTvShows, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAppUserTvShows(@AuthenticationPrincipal AppUser appUser) {
        List<AppUserTvShow> userTvShows = service.findAllUserTvShows(appUser);
        return new ResponseEntity<>(userTvShows, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAppUserTvShow(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody TvShow tvShow
    ) {
        Result<AppUserTvShow> result = service.create(tvShow, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping
    public ResponseEntity<?> updateAppUserTvShow(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody AppUserTvShow userTvShow
    ) {
        Result<AppUserTvShow> result = service.update(userTvShow, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAppUserTvShow(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody AppUserTvShow userTvShow
    ) {
        Result<?> result = service.delete(userTvShow, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}

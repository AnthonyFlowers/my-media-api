package mymedia.controllers;

import mymedia.domain.AppUserMovieService;
import mymedia.domain.Result;
import mymedia.models.AppUserMovie;
import mymedia.models.Movie;
import mymedia.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user/movie")
public class AppUserMovieController {

    private final AppUserMovieService service;

    public AppUserMovieController(AppUserMovieService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getUserMovies(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Page<AppUserMovie> movies = service.findUserMovies(page, pageSize, appUser);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserMovies(
            @AuthenticationPrincipal AppUser appUser) {
        List<AppUserMovie> movies = service.findAllUserMovies(appUser);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{appUserMovieId}")
    public ResponseEntity<?> getUserMovie(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable int appUserMovieId) {
        AppUserMovie movie = service.findByUserMovieIdAndUser(appUserMovieId, appUser);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUserMovieEntry(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody Movie movie) {
        Result<AppUserMovie> result = service.create(appUser, movie);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping
    public ResponseEntity<?> updateUserMovieEntry(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody AppUserMovie userMovie) {
        Result<AppUserMovie> result = service.update(userMovie, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserMovieEntry(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody AppUserMovie userMovie) {
        Result<?> result = service.delete(userMovie, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}

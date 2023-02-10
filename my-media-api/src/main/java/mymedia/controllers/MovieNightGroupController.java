package mymedia.controllers;

import mymedia.domain.MovieNightGroupService;
import mymedia.domain.Result;
import mymedia.models.MovieNightGroup;
import mymedia.security.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/movie-night/group")
public class MovieNightGroupController {
    private final MovieNightGroupService service;

    public MovieNightGroupController(MovieNightGroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        return new ResponseEntity<>(service.findMovieNightGroups(), HttpStatus.OK);
    }

    @GetMapping("/with-top-movies")
    public ResponseEntity<?> getAllGroupsWithTopMovies() {
        return new ResponseEntity<>(service.findGroupsWithTopMovies(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveNewGroup(@RequestBody MovieNightGroup group, @AuthenticationPrincipal AppUser admin) {
        Result<MovieNightGroup> result = service.saveNewGroup(group, admin);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return ErrorResponse.build(result);
        }
    }
}

package mymedia.controllers;

import mymedia.domain.MovieNightGroupService;
import mymedia.models.MovieNightGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/movie-night/group")
public class MovieNightGroupController {
    private final MovieNightGroupService service;

    public MovieNightGroupController(MovieNightGroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        List<MovieNightGroup> groups = service.findMovieNightGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}

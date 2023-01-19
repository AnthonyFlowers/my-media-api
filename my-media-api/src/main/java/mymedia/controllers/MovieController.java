package mymedia.controllers;

import mymedia.domain.MovieService;
import mymedia.domain.Result;
import mymedia.models.Movie;
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
@RequestMapping("/api/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<?> getMovies(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int pageSize,
            @RequestParam(required = false) String title) {
        Page<Movie> movies = movieService.findMovies(page, pageSize);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
                .body(movies);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentMovies(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<Movie> movies = movieService.findRecentMovies(page, pageSize);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getMoviesSearch(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int pageSize,
            @RequestParam(required = false) String title) {
        Result<Page<Movie>> result = movieService.search(page, pageSize, title);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
}

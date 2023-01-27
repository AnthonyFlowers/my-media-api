package mymedia.domain;

import jakarta.validation.Validator;
import mymedia.data.MovieRepository;
import mymedia.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final Validator validator;
    private final int defaultPageSize = 50;
    private final int defaultSmallPageSize = 10;
    private final PageRequest smallPr;

    public MovieService(MovieRepository movieRepository, Validator validator) {
        this.movieRepository = movieRepository;
        this.validator = validator;
        this.smallPr = PageRequest.of(0, 10);
        ;
    }

    public Page<Movie> findMovies(int page, int pageSize) {
        page = Math.max(page - 1, 0);
        pageSize = pageSize <= 0 ? defaultSmallPageSize : pageSize;
        return movieRepository.findAll(PageRequest.of(
                page, pageSize,
                Sort.by(Sort.Direction.DESC, "movieYear")
        ));
    }

    public Page<Movie> findRecentMovies(Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = defaultSmallPageSize;
        }
        return movieRepository.findAll(PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "movieYear"))
        );
    }

    public void update(Movie movie) {
        movieRepository.save(movie);
    }

    public Movie findById(int movieId) {
        return movieRepository.findById(movieId).orElse(null);
    }

    public Result<Page<Movie>> search(int page, int pageSize, String title) {
        Result<Page<Movie>> result = new Result<>();
        if (title == null || title.isBlank()) {
            result.addMessage(ResultType.INVALID, "can not search with null or blank title");
        }
        if (result.isSuccess()) {
            Page<Movie> movies = movieRepository.findByMovieNameContainsIgnoreCase(
                    PageRequest.of(page - 1, pageSize),
                    title
            );
            if (movies.isEmpty()) {
                result.addMessage(ResultType.NOT_FOUND, "did not find any results");
            } else {
                result.setPayload(movies);
            }
        }
        return result;
    }

    public Movie findByMovieName(String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        return movieRepository.findFirstByMovieName(title);
    }
}

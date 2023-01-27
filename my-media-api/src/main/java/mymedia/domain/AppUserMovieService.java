package mymedia.domain;

import jakarta.validation.Validator;
import mymedia.data.AppUserMovieRepository;
import mymedia.models.AppUserMovie;
import mymedia.models.Movie;
import mymedia.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserMovieService {

    private final AppUserMovieRepository repository;
    private final MovieService movieService;
    private final Validator validator;

    public AppUserMovieService(AppUserMovieRepository repository, MovieService movieService, Validator validator) {
        this.repository = repository;
        this.movieService = movieService;
        this.validator = validator;
    }

    public AppUserMovie findByUserMovieIdAndUser(int userMovieId, AppUser user) {
        return repository.findByAppUserMovieIdAndUser(userMovieId, user);
    }

    public AppUserMovie findByUserMovieId(int userMovieId) {
        return repository.findById(userMovieId).orElse(null);
    }

    public Page<AppUserMovie> findUserMovies(int page, int pageSize, AppUser user) {
        return repository.findByUserUsername(
                PageRequest.of(
                        Math.max(page - 1, 0),
                        pageSize <= 0 ? 10 : pageSize
                ),
                user.getUsername()
        );
    }

    public List<AppUserMovie> findAllUserMovies(AppUser user) {
        return repository.findByUserUsername(user.getUsername());
    }

    public Result<AppUserMovie> create(AppUser user, Movie movie) {
        Result<AppUserMovie> result = verifyMovieEntry(user, movie);
        if (result.isSuccess()) {
            AppUserMovie appUserMovie = new AppUserMovie();
            appUserMovie.setMovie(movie);
            appUserMovie.setUser(user);
            validator.validate(appUserMovie).forEach((vi) ->
                    result.addMessage(ResultType.INVALID, vi.getMessage()));
            if (result.isSuccess()) {
                result.setPayload(repository.save(appUserMovie));
            }
        }
        return result;
    }

    private Result<AppUserMovie> verifyMovieEntry(AppUser user, Movie movie) {
        Result<AppUserMovie> result = new Result<>();
        if (movie == null) {
            result.addMessage(ResultType.INVALID, "Can not add null movie entry");
            return result;
        }
        if (movieService.findById(movie.getMovieId()) == null) {
            result.addMessage(ResultType.NOT_FOUND, "Could not find that movie");
        }
        AppUserMovie foundUserMovie = repository.findByUserAppUserIdAndMovieMovieId(
                user.getAppUserId(),
                movie.getMovieId()
        );
        if (foundUserMovie != null) {
            result.addMessage(ResultType.INVALID, "That user already has an entry for that movie");
            return result;
        }
        return result;
    }

    public Result<AppUserMovie> update(AppUserMovie userMovie, AppUser appUser) {
        Result<AppUserMovie> result = new Result<>();
        AppUserMovie foundAppUserMovie = findByUserMovieId(userMovie.getAppUserMovieId());
        if (foundAppUserMovie == null) {
            result.addMessage(ResultType.NOT_FOUND, "Could not find that movie entry to update");
        } else if (foundAppUserMovie.getUserId() != appUser.getAppUserId()) {
            result.addMessage(ResultType.INVALID, "Could not update that movie entry");
        } else {
            userMovie.setMovie(foundAppUserMovie.getMovie());
            userMovie.setUser(appUser);
            validator.validate(userMovie).forEach((vi) ->
                    result.addMessage(ResultType.INVALID, vi.getMessage()));
            if (result.isSuccess()) {
                result.setPayload(repository.save(userMovie));
            }
        }
        return result;
    }

    public Result<?> delete(AppUserMovie userMovie, AppUser appUser) {
        Result<?> result = new Result<>();
        AppUserMovie foundAppUserMovie = findByUserMovieId(userMovie.getAppUserMovieId());
        if (foundAppUserMovie == null) {
            result.addMessage(ResultType.NOT_FOUND, "Could not find that movie entry");
            return result;
        }
        if (foundAppUserMovie.getUserId() != appUser.getAppUserId()) {
            result.addMessage(ResultType.INVALID, "Could not delete that movie entry");
        } else {
            repository.delete(foundAppUserMovie);
        }
        return result;
    }
}

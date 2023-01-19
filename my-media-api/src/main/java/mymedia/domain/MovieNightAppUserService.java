package mymedia.domain;

import mymedia.data.MovieNightAppUserRepository;
import mymedia.models.Movie;
import mymedia.models.MovieNightAppUser;
import mymedia.security.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieNightAppUserService {

    private final MovieNightAppUserRepository repository;

    public MovieNightAppUserService(MovieNightAppUserRepository repository) {
        this.repository = repository;
    }

    public List<MovieNightAppUser> findByAppUser(AppUser user) {
        return repository.findByAppUserId(user.getAppUserId());
    }

    public List<MovieNightAppUser> findByGroupId(int groupId) {
        return repository.findByGroupId(groupId);
    }

    public Movie getTopMovie(int groupId) {
//        return repository.findTopMovieByGroupId(groupId);
    return null;
    }
}

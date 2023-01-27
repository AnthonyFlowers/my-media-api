package mymedia.domain;

import mymedia.data.MovieNightAppUserRepository;
import mymedia.models.MovieNightAppUser;
import mymedia.models.MovieNightGroup;
import mymedia.security.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieNightAppUserService {

    private final MovieNightAppUserRepository repository;

    public MovieNightAppUserService(MovieNightAppUserRepository repository) {
        this.repository = repository;
    }

    public List<MovieNightAppUser> findByGroupId(int groupId) {
        return repository.findByGroupId(groupId);
    }

    public List<MovieNightAppUser> findByAppUserId(int userId) {
        return repository.findByAppUserId(userId);
    }

    public Result<MovieNightAppUser> addAdmin(int adminUserId, int groupId) {
        Result<MovieNightAppUser> result = new Result<>();
        MovieNightAppUser movieNightAppUser = new MovieNightAppUser();
        movieNightAppUser.setAppUserId(adminUserId);
        movieNightAppUser.setIsModerator(true);
        movieNightAppUser.setGroupId(groupId);
        MovieNightAppUser adminAppUser = repository.save(movieNightAppUser);
        if(adminAppUser.getAppUserGroupId() < 1){
            result.addMessage(ResultType.INVALID, "Could not create that group admin");
        } else {
            result.setPayload(adminAppUser);
        }
        return result;
    }
}

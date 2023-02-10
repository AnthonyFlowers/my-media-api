package mymedia.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mymedia.data.GroupWithTopMovie;
import mymedia.data.MovieNightGroupRepository;
import mymedia.models.MovieNightGroup;
import mymedia.security.AppUser;
import mymedia.security.AppUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class MovieNightGroupService {
    private final MovieNightGroupRepository repository;
    private final AppUserService appUserService;
    private final MovieNightAppUserService movieNightAppUserService;
    private final Validator validator;

    public MovieNightGroupService(MovieNightGroupRepository repository, AppUserService appUserService, MovieNightAppUserService movieNightAppUserService, Validator validator) {
        this.repository = repository;
        this.appUserService = appUserService;
        this.movieNightAppUserService = movieNightAppUserService;
        this.validator = validator;
    }

    public List<MovieNightGroup> findMovieNightGroups() {
        return repository.findAll();
    }

    public List<GroupWithTopMovie> findGroupsWithTopMovies() {
        return repository.findGroupsWithTopMovies();
    }

    public Result<MovieNightGroup> saveNewGroup(MovieNightGroup group, AppUser admin) {
        Result<MovieNightGroup> result = validate(group);
        if (!result.isSuccess()) {
            return result;
        }
        List<AppUser> appUsers = appUserService.findAllByUsernames(group.getUsers());
        group.setUsers(appUsers);
        MovieNightGroup savedGroup = repository.save(group);
        movieNightAppUserService.addAdmin(admin.getAppUserId(), savedGroup.getGroupId());
        result.setPayload(group);
        return result;
    }

    private Result<MovieNightGroup> validate(MovieNightGroup group) {
        Result<MovieNightGroup> result = new Result<>();
        Set<ConstraintViolation<MovieNightGroup>> validate = validator.validate(group);
        validate.forEach((v) -> {
            result.addMessage(ResultType.INVALID, v.getMessage());
        });
        return result;
    }
}

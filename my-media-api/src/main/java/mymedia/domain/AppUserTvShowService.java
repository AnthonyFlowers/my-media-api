package mymedia.domain;

import mymedia.data.AppUserTvShowRepository;
import mymedia.models.AppUserTvShow;
import mymedia.models.TvShow;
import mymedia.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;

@Service
public class AppUserTvShowService {

    private final AppUserTvShowRepository repository;
    private final TvShowService tvShowService;
    private final Validator validator;
    private final int defaultSmallPageSize = 10;

    public AppUserTvShowService(AppUserTvShowRepository repository, TvShowService tvShowService, Validator validator) {
        this.repository = repository;
        this.tvShowService = tvShowService;
        this.validator = validator;
    }


    public Page<AppUserTvShow> findUserTvShows(int page, int pageSize, AppUser appUser) {
        return repository.findByUserUsername(PageRequest.of(
                        Math.max(page, 0), pageSize < 0 ? defaultSmallPageSize : pageSize),
                appUser.getUsername()
        );
    }

    public Result<AppUserTvShow> create(TvShow tvShow, AppUser appUser) {
        Result<AppUserTvShow> result = verifyTvShowEntry(tvShow, appUser);
        if (result.isSuccess()) {
            AppUserTvShow appUserTvShow = new AppUserTvShow(tvShow, appUser);
            validator.validate(appUserTvShow).forEach((vi) ->
                    result.addMessage(ResultType.INVALID, vi.getMessage()));
            if (result.isSuccess()) {
                result.setPayload(repository.save(appUserTvShow));
            }
        }
        return result;
    }

    private Result<AppUserTvShow> verifyTvShowEntry(TvShow tvShow, AppUser appUser) {
        Result<AppUserTvShow> result = new Result<>();
        if (tvShow == null) {
            result.addMessage(ResultType.INVALID, "Cannot add an entry with a null TV Show");
            return result;
        }
        if (tvShowService.findById(tvShow.getTvShowId()) == null) {
            result.addMessage(ResultType.NOT_FOUND, "Could not find that TV Show");
        }
        AppUserTvShow found = repository.findByUserAppUserIdAndTvShowTvShowId(
                appUser.getAppUserId(),
                tvShow.getTvShowId()
        );
        if (found != null) {
            result.addMessage(ResultType.INVALID, "User already has an entry for that TV Show");
        }
        return result;
    }

    public Result<?> delete(AppUserTvShow userTvShow, AppUser appUser) {
        Result<?> result = new Result<>();
        AppUserTvShow foundAppUserTvShow = findByUserTvShowId(userTvShow.getAppUserTvShowId());
        if (foundAppUserTvShow == null) {
            result.addMessage(ResultType.NOT_FOUND, "Could not find that tv show entry");
            return result;
        }
        if (foundAppUserTvShow.getUserId() != appUser.getAppUserId()) {
            result.addMessage(ResultType.INVALID, "Could not delete that tv show entry");
        } else {
            repository.delete(foundAppUserTvShow);
        }
        return result;
    }

    private AppUserTvShow findByUserTvShowId(int appUserTvShowId) {
        return repository.findById(appUserTvShowId).orElse(null);
    }

    public Result<AppUserTvShow> update(AppUserTvShow userTvShow, AppUser appUser) {
        Result<AppUserTvShow> result = new Result<>();
        validator.validate(userTvShow).forEach((vi) ->
                result.addMessage(ResultType.INVALID, vi.getMessage()));
        if(!result.isSuccess()){
            return result;
        }
        AppUserTvShow foundAppUserTvShow = findByUserTvShowId(userTvShow.getAppUserTvShowId());
        if (foundAppUserTvShow == null) {
            result.addMessage(ResultType.NOT_FOUND, "Could not find that tv show entry to update");
        } else if (foundAppUserTvShow.getUserId() != appUser.getAppUserId()) {
            result.addMessage(ResultType.INVALID, "Could not update that tv show entry");
        } else {
            userTvShow.setTvShow(foundAppUserTvShow.getTvShow());
            userTvShow.setUser(appUser);
            if (result.isSuccess()) {
                result.setPayload(repository.save(userTvShow));
            }
        }
        return result;
    }

    public List<AppUserTvShow> findAllUserTvShows(AppUser appUser) {
        return repository.findByUserUsername(appUser.getUsername());
    }
}

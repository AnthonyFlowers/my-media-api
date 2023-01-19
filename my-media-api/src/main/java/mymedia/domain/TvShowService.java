package mymedia.domain;

import mymedia.data.TvShowRepository;
import mymedia.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TvShowService {
    private final TvShowRepository repository;

    public TvShowService(TvShowRepository repository) {
        this.repository = repository;
    }

    public Page<TvShow> findTvShows(int page, int pageSize) {
        return repository.findAll(PageRequest.of(
                page - 1, pageSize,
                Sort.by(Sort.Direction.DESC, "releaseYear"))
        );
    }

    public TvShow findById(int tvShowId) {
        return repository.findById(tvShowId).orElse(null);
    }

    public Result<Page<TvShow>> search(int page, int pageSize, String title) {
        Result<Page<TvShow>> result = new Result<>();
        if (title == null || title.isBlank()) {
            result.addMessage(ResultType.INVALID, "can not search with null or blank title");
        }
        if (result.isSuccess()) {
            Page<TvShow> tvShows = repository.findByTvShowNameContainsIgnoreCase(
                    PageRequest.of(page - 1, pageSize),
                    title
            );
            if (tvShows.isEmpty()) {
                result.addMessage(ResultType.NOT_FOUND, "did not find any results");
            } else {
                result.setPayload(tvShows);
            }
        }
        return result;
    }
}

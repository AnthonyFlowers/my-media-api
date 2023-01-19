package mymedia.domain;

import mymedia.data.MovieNightGroupRepository;
import mymedia.models.MovieNightGroup;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieNightGroupService {
    private final MovieNightGroupRepository repository;
    private final Validator validator;

    public MovieNightGroupService(MovieNightGroupRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<MovieNightGroup> findMovieNightGroups() {
        return repository.findAll();
    }
}

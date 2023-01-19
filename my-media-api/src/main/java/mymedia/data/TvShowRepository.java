package mymedia.data;

import mymedia.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Integer> {
    Page<TvShow> findByTvShowNameContainsIgnoreCase(PageRequest pageRequest, String name);
}

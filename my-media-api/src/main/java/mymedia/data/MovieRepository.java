package mymedia.data;

import mymedia.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Page<Movie> findByMovieNameContainsIgnoreCase(PageRequest pr, String name);

    Movie findFirstByMovieName(String title);
}

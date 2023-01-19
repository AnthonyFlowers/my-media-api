package mymedia.data;

import mymedia.models.MovieNightGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieNightGroupRepository extends JpaRepository<MovieNightGroup, Integer> {
}

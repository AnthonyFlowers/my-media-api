package mymedia.data;

import mymedia.models.MovieNightAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieNightAppUserRepository extends JpaRepository<MovieNightAppUser, Integer> {

    List<MovieNightAppUser> findByAppUserId(int userId);

    List<MovieNightAppUser> findByGroupId(int groupId);

}

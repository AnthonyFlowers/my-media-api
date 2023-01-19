package mymedia.data;

import mymedia.models.Movie;
import mymedia.models.MovieNightAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieNightAppUserRepository extends JpaRepository<MovieNightAppUser, Integer> {

    List<MovieNightAppUser> findByAppUserId(int userId);

    List<MovieNightAppUser> findByGroupId(int groupId);

    //select x from MovieNightAppUser x group by x.user_vote order by count(x) desc
    @Query("""
            select mau.userMovieVote from MovieNightAppUser mau
            where mau.userMovieVote is not null
            and mau.groupId = ?1
            group by mau.userMovieVote
            order by mau.userMovieVote desc
            """)
    Movie findTopMovieByGroupId(int groupId);
}

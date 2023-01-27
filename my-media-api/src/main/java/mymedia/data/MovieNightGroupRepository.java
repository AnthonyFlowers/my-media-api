package mymedia.data;

import mymedia.models.Movie;
import mymedia.models.MovieNightGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieNightGroupRepository extends JpaRepository<MovieNightGroup, Integer> {

    @Query("""
            select mng as group, mngau.userMovieVote as topMovie
            from MovieNightGroup mng join MovieNightAppUser mngau
            on mng.groupId = mngau.groupId
            group by mng.groupId, mngau.userMovieVote
            having count(mngau.userMovieVote) = (
            	select max(user_vote_count) from (
            		select count(mnau2.userMovieVote) as user_vote_count
            		from MovieNightAppUser mnau2
                    where mnau2.groupId = mng.groupId
            		group by mnau2.groupId, mnau2.userMovieVote
            	) t2
            )
            """)
    List<GroupWithTopMovie> findGroupsWithTopMovies();
}

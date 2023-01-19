package mymedia.data;

import mymedia.models.Movie;
import mymedia.models.MovieNightGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MovieNightGroupRepository extends JpaRepository<MovieNightGroup, Integer> {

    @Query("""
            select mng.groupId, mngau.userMovieVote from MovieNightGroup mng join MovieNightAppUser mngau
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
    Map<MovieNightGroup, Movie> findGroupsWithTopMovies(int groupId);

//    @Query("""
//            select mng, mnau.userMovieVote
//            from MovieNightGroup mng left join MovieNightAppUser mnau
//            on mng.groupId = mnau.groupId
//            group by mng.groupId
//            having count(mnau) = (
//                select max(count(mnau2.userMovieVote))
//                from MovieNightGroup mng2 join MovieNightAppUSer mnau2
//                on mng2.groupId = mnau2.groupId
//                group by mng2.groupId
//            )
//            """)
//    Map<MovieNightGroup, Movie> findGroupsWithTopMovies(int groupId);

//            group by mng.groupId
//            having count(mnau) = (
//                select max(count(mnau.userMovieVote))
//                from MovieNightGroup mng2 join MovieNightAppUSer mnau2
//                on mng2.groupId = mnau2.groupId
//                group by mng2.groupId
//            )
}

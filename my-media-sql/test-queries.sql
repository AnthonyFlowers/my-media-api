use my_media;
use my_media_test;
call set_known_good_state();
select * from app_user;
select * from app_user_movie;
select * from app_user_role;
select * from app_role;
select * from tv_show;
select * from movie;

select username, movie_name from app_user a join app_user_movie am join movie m
    on a.app_user_id = am.app_user_id
    and am.movie_id = m.movie_id;

select username, tv_show_name from app_user a join app_user_tv_show ats join tv_show ts
    on a.app_user_id = ats.app_user_id
    and ats.tv_show_id = ts.tv_show_id;
select * from movie;
select * from movie
where movie_name = "Iron Man"
and movie_year = 2008;
select count(*) from movie;

select * from tv_show;
select count(*) from app_user;

select * from movie_night_group;

select user_vote, count(*) as votes from movie_night_app_user 
where user_vote is not null
group by user_vote
limit 1;

select mng.group_id, mngau.user_vote
from movie_night_group mng join movie_night_app_user mngau
on mng.group_id = mngau.group_id
group by mng.group_id, mngau.user_vote
having count(mngau.user_vote) = (
	select max(user_vote_count) from (
		select count(mnau2.user_vote) as user_vote_count
		from movie_night_app_user mnau2
        where mnau2.group_id = mng.group_id
		group by mnau2.group_id, mnau2.user_vote
	) t2
);

-- select mng as group, mngau.userMovieVote as topMovie
-- from MovieNightGroup mng join MovieNightAppUser mngau
-- on mng.groupId = mngau.groupId
-- group by mng.groupId, mngau.userMovieVote
-- having count(mngau.userMovieVote) = (
-- 	select max(user_vote_count) from (
-- 		select count(mnau2.userMovieVote) as user_vote_count
-- 		from MovieNightAppUser mnau2
-- 		where mnau2.groupId = mng.groupId
-- 		group by mnau2.groupId, mnau2.userMovieVote
-- 	) t2
-- )

insert into movie_night_app_user (app_user_id, group_id, user_vote) values
(1,2,2),
(1,2,2),
(1,2,2);

select * from movie_night_app_user;
select * from movie_night_group mng join movie_night_app_user mnau
on mng.group_id = mnau.group_id;
select max(cnt) from (
	select count(user_vote) as cnt
	from movie_night_app_user mnau2
	group by mnau2.group_id, mnau2.user_vote
) t2;

select * from movie_night_group;
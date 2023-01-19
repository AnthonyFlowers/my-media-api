use my_media;
set sql_safe_updates = 0;

delete from app_user_role;
alter table app_user_role auto_increment = 1;
delete from app_user_movie;
delete from app_user_tv_show;
delete from app_user;
alter table app_user auto_increment = 1;
delete from tv_show_season_tv_show;
alter table tv_show_season_tv_show auto_increment = 1;
delete from tv_show_episode;
alter table tv_show_episode auto_increment = 1;
delete from tv_show_season;
alter table tv_show_season auto_increment = 1;
delete from tv_show;
alter table tv_show auto_increment = 1;
-- delete from movie;
-- alter table movie auto_increment = 1;

insert into tv_show (tv_show_name) values
    ('Rick and Morty'), -- test read
    ('Suits'), -- test update
    ('Stranger Things'); -- test delete
    -- test create ('Tokyo Ghoul')

insert into tv_show_season (tv_show_id, tv_show_season_index) values
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1), -- test update
    (2, 2), -- test delete
    (2, 3); -- test read
    -- test create (1, 4)

insert into tv_show_season_tv_show (tv_show_season_id, tv_show_id) values
    (1, 1),
    (2, 2);

insert into tv_show_episode (tv_show_season_id, tv_show_episode_index) values
    (1, 1),
    (1, 2), -- test read
    (1, 3),
    (1, 4), -- test update
    (4, 1),
    (4, 2),
    (4, 3), -- test delete
    (4, 4);
    -- test create (1, 5)

-- P@ssw0rd!
insert into app_user (username, password_hash) values
    ('johnsmith', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'),
    ('janedoe', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'),
    ('ashketchum', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa');

insert into app_user_movie (app_user_id, movie_id) values
    (1, (select movie_id from movie
where movie_name = "Iron Man"
and movie_year = 2008)),
    (1, 2),
    (2, 1);

insert into app_user_tv_show (app_user_id, tv_show_id) values
    (1, 1),
    (1, 2),
    (2, 2);

insert into app_user_role (app_user_id, app_role_id) values
    (1, 1),
    (2, 2);

set sql_safe_updates = 1;

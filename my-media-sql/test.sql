drop database if exists my_media_test;
create database my_media_test;
use my_media_test;

create table movie (
    movie_id int primary key auto_increment,
    movie_name varchar(256) not null,
    movie_year int,
    movie_length int,
    movie_overview varchar(2048)
);

create table tv_show (
    tv_show_id int primary key auto_increment,
    tv_show_name varchar(256) not null,
    tv_show_overview varchar(2048),
    tv_show_release_year int not null
);

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default 1
);

create table movie_night_group (
	group_id int primary key auto_increment,
    group_name varchar(50) not null
 );

create table movie_night_group_movie (
	group_id int not null,
    movie_id int not null,
    constraint fk_movie_night_group_movie_group_id
		foreign key (group_id)
        references movie_night_group(group_id),
	constraint fk_movie_night_group_movie_movie_id
		foreign key (movie_id)
        references movie(movie_id),
    constraint pk_movie_night_group_movie
		primary key (group_id, movie_id)
);

create table movie_night_app_user (
	app_user_movie_night_group_id int primary key auto_increment,
	app_user_id int not null,
    group_id int not null,
    moderator boolean default false,
    user_vote int,
	constraint fk_movie_night_app_user_app_user_id
		foreign key (app_user_id)
        references app_user(app_user_id),
	constraint fk_movie_night_app_user_movie_night_group
		foreign key (group_id)
        references movie_night_group(group_id),
	constraint fk_movie_night_app_user_movie_night_group_movie_movie_id
		foreign key (user_vote)
        references movie_night_group_movie(movie_id)
);

create table app_user_movie (
	app_user_movie_id int primary key auto_increment,
    app_user_id int not null,
    movie_id int not null,
    watch_count int default 1,
    watched bool not null default false,
    constraint fk_app_user_movie_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_movie_movie_id
        foreign key (movie_id)
        references movie(movie_id),
	constraint uq_user_id_movie_id
		unique (app_user_id, movie_id)
);

create table app_user_tv_show (
	app_user_tv_show_id int primary key auto_increment,
    app_user_id int not null,
    tv_show_id int not null,
    season int default 0,
    episode int default 0,
    watched bool not null default false,
    watch_count int default 0,
    constraint fk_app_user_tv_show_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_tv_show_tv_show_id
        foreign key (tv_show_id)
        references tv_show(tv_show_id)
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

insert into app_role (`name`) values
    ('ADMIN'),
    ('USER');

delimiter //
create procedure set_known_good_state()
begin
	set sql_safe_updates = 0;

delete from movie_night_app_user;
    alter table movie_night_app_user auto_increment = 1;
delete from movie_night_group_movie;
delete from movie_night_group;
    alter table movie_night_group auto_increment = 1;
	delete from app_user_role;
	alter table app_user_role auto_increment = 1;
	delete from app_user_movie;
    alter table app_user_movie auto_increment = 1;
	delete from app_user_tv_show;
    alter table app_user_tv_show auto_increment = 1;
	delete from app_user;
	alter table app_user auto_increment = 1;
	delete from tv_show;
	alter table tv_show auto_increment = 1;
	delete from movie;
	alter table movie auto_increment = 1;

    insert into movie (movie_name, movie_year, movie_length, movie_overview) values
        ('Iron Man', 2008, 126, 'After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.'), -- test read
        ('Iron Man 2', 2010, 125, 'With the world now aware of his dual life as the armored superhero Iron Man, billionaire inventor Tony Stark faces pressure from the government, the press and the public to share his technology with the military. Unwilling to let go of his invention, Stark, with Pepper Potts and James ''Rhodey'' Rhodes at his side, must forge new alliances – and confront powerful enemies.'), -- test update
        ('Iron Man 3', 2013, 130, 'When Tony Stark''s world is torn apart by a formidable terrorist called the Mandarin, he starts an odyssey of rebuilding and retribution.'); -- test delete
        -- test create ('Spider-Man', null, 121)

    insert into tv_show (tv_show_name, tv_show_release_year) values
        ('Rick and Morty', 2013), -- test read
        ('Suits', 2011), -- test update
        ('Stranger Things', 2016),
        ('Shark Tank', 2009); -- test delete
        -- test create ('Tokyo Ghoul')

    -- P@ssw0rd!
    insert into app_user (username, password_hash) values
        ('johnsmith', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'),
        ('janedoe', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'),
        ('ashketchum', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa');

    insert into app_user_movie (app_user_id, movie_id) values
        (1, 1), -- read
        (1, 2), -- update
        (2, 1); -- delete
        -- test create (2, 1)

    insert into app_user_tv_show (app_user_id, tv_show_id, season, episode) values
        (1, 1, 1, 5), -- read
        (1, 2, 1, 1), -- update (1, 2, 2, 1)
        (2, 2, 4, 3); -- delete
        -- test create (1, 4, 1, 1)

    insert into app_user_role (app_user_id, app_role_id) values
        (1, 1),
        (2, 2);
        
	insert into movie_night_group (group_name) values
		("Test Group"), -- read
        ("Friends"), -- update ("Discord Friends")
        ("Online"), -- delete
        ("Extra Group");
        -- test create ("New Group")
        
	insert into movie_night_group_movie (group_id, movie_id) values
		(1, 1), -- read
        (1, 2),
        (2, 1); -- delete
        -- test create (2, 2)
        
	insert into movie_night_app_user (app_user_id, group_id, moderator, user_vote) values
		(1, 1, true, 1), -- read
        (2, 4, true, null), -- update (2, 4, true, 1)
        (2, 1, false, 1), -- read
        (2, 2, true, null); -- delete
        -- test create "adding a user to a group" (1, 4, false, null)
	

    set sql_safe_updates = 1;
end //
delimiter ;

call set_known_good_state();

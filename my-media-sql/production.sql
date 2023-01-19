drop database if exists my_media;
create database my_media;
use my_media;

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
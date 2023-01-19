use my_media;

delete from app_user_movie
where app_user_id > 0;
alter table app_user_movie auto_increment = 1;
delete from app_user_role
where app_user_id > 2;

delete from app_user
where app_user_id > 2;
alter table app_user auto_increment = 3;
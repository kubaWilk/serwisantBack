alter table if exists users drop constraint if exists FKfsi9283rk1akvfmlbfrhdn5vj;
drop table if exists user_details cascade;
drop table if exists users cascade;

create table if not exists user_details (
    user_details_id serial not null,
    first_name varchar(255),
    last_name varchar(255),
    street varchar(255),
    post_code varchar(255),
    city varchar(255),
    primary key (user_details_id)
                          );

create table if not exists  users (
    user_id serial not null,
    username varchar(255),
    password varchar(255),
    email varchar(255),
    user_detail_id integer unique,
    primary key (user_id)
                   );

alter table if exists users add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id) references user_details;

insert into user_details (first_name, last_name, street, post_code, city)
    values ('root', 'root', 'root', 'root', 'root');

insert into users (username, password, email, user_detail_id)
    values ('root', 'root', 'root@root.com', 1);


alter table if exists authorities drop constraint if exists FKk91upmbueyim93v469wj7b2qh;
alter table if exists password_reset_token drop constraint if exists FK83nsrttkwkb6ym0anu051mtxn;
alter table if exists users drop constraint if exists FKlbumi6chkcxaxk2m91y429dv1;
alter table if exists users drop constraint if exists FKfsi9283rk1akvfmlbfrhdn5vj;
drop table if exists authorities cascade;
drop table if exists password_reset_token cascade;
drop table if exists user_details cascade;
drop table if exists users cascade;
drop sequence if exists password_reset_token_seq;


create sequence password_reset_token_seq start with 1 increment by 50;

create table if not exists user_details (
    user_details_id serial not null,
    first_name varchar(255),
    last_name varchar(255),
    street varchar(255),
    post_code varchar(255),
    city varchar(255),
    primary key (user_details_id)
                          );

create table users (
    active boolean,
    user_detail_id integer not null unique,
    user_id serial not null,
    reset_token_id bigint unique,
    email varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (user_id));

create table authorities (
    authority_id serial not null primary key,
    user_id serial not null,
    username varchar(50) not null,
    authority varchar(50) not null
);

create table password_reset_token (
    user_id integer not null unique,
    expiry_date timestamp(6),
    id bigint not null,
    token varchar(255),
    primary key (id)
                                  );

alter table if exists authorities
    add constraint FKk91upmbueyim93v469wj7b2qh
        foreign key (user_id) references users;

alter table if exists password_reset_token
    add constraint FK83nsrttkwkb6ym0anu051mtxn
        foreign key (user_id) references users;

alter table if exists users
    add constraint FKlbumi6chkcxaxk2m91y429dv1
        foreign key (reset_token_id)
            references password_reset_token;

alter table if exists users
    add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id)
        references user_details;

insert into user_details (first_name, last_name, street, post_code, city)
    values ('root', 'root', 'root', 'root', 'root');

insert into users (username, password, active, email, user_detail_id)
     values ('root', '$2a$12$enZyMcOqcaSgS4oPxthFweOfCvY5zqkEHi1lwy7VZ2QWCfLF9P8/2', true, 'root@root.com', 1);

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_CUSTOMER');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_EMPLOYEE');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_ADMIN');
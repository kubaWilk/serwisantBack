alter table if exists authorities drop constraint if exists FKlcdevx7gs0kv62awadnvy4nle;
alter table if exists authorities drop constraint if exists FKk91upmbueyim93v469wj7b2qh;
alter table if exists users drop constraint if exists FKfsi9283rk1akvfmlbfrhdn5vj;
drop table if exists authorities cascade;
drop table if exists role cascade;
drop table if exists user_details cascade;
drop table if exists users cascade;


-- create table authorities (
--     role_id integer not null,
--     user_id integer not null
--                          );


-- create table role (
--     id serial not null,
--     role varchar(255),
--     primary key (id)
--                   );

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
    username varchar(255) unique,
    password varchar(255),
    active boolean not null,
    email varchar(255),
    user_detail_id integer unique,
    primary key (user_id)
                   );

create table authorities (
                             username varchar(50) not null,
                             authority varchar(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);

-- alter table if exists authorities
--     add constraint FKlcdevx7gs0kv62awadnvy4nle foreign key (role_id)
--         references role;
-- alter table if exists authorities
--     add constraint FKk91upmbueyim93v469wj7b2qh foreign key (username)
--         references users(username);
alter table if exists users
    add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id)
        references user_details;
--
-- insert into role (id, role) values (1, 'ROLE_CUSTOMER');
-- insert into role (id, role) values (2, 'ROLE_EMPLOYEE');
-- insert into role (id, role) values (3, 'ROLE_ADMIN');

insert into user_details (first_name, last_name, street, post_code, city)
    values ('root', 'root', 'root', 'root', 'root');

insert into users (username, password, active, email, user_detail_id)
    values ('root', '{noop}root', true, 'root@root.com', 1);

-- insert into authorities (role_id, user_id) values(1,1);
-- insert into authorities (role_id, user_id) values(2,1);
-- insert into authorities (role_id, user_id) values(3,1);

insert into authorities (username, authority)
    values ('root', 'ROLE_EMPLOYEE');
-- ### DROP DB ###
alter table if exists authorities drop constraint if exists FKk91upmbueyim93v469wj7b2qh;
alter table if exists cost drop constraint if exists FKskp0e42nvl99cjhjof3fltbcg;
alter table if exists note drop constraint if exists FK94ucwcm02pj66wdvxmnvahpqp;
alter table if exists password_reset_token drop constraint if exists FK83nsrttkwkb6ym0anu051mtxn;
alter table if exists repair drop constraint if exists FKe2lm4qyk4g36lkdab4sqfxnwf;
alter table if exists repair drop constraint if exists FKixsc0illvsi63j93mh26w003c;
alter table if exists repair_costs drop constraint if exists FKtovovsvew76ohqhu2ytulwh0;
alter table if exists repair_costs drop constraint if exists FKmb1u5kjqqqrlb1pnmkw05xapc;
alter table if exists users drop constraint if exists FKlbumi6chkcxaxk2m91y429dv1;
alter table if exists users drop constraint if exists FKfsi9283rk1akvfmlbfrhdn5vj;
drop table if exists authorities cascade;
drop table if exists cost cascade;
drop table if exists device cascade;
drop table if exists note cascade;
drop table if exists password_reset_token cascade;
drop table if exists repair cascade;
drop table if exists repair_costs cascade;
drop table if exists user_details cascade;
drop table if exists users cascade;
drop sequence if exists password_reset_token_seq;

-- ### CREATE DB ###
create sequence password_reset_token_seq start with 1 increment by 50;

create table authorities (
     authority_id serial not null primary key,
     user_id serial not null,
     username varchar(50) not null,
     authority varchar(50) not null
);

create table cost (
    cost_type smallint check (cost_type between 0 and 1),
    id serial not null,
    price float4,
    repair_id integer not null,
    primary key (id)
);

create table device (
    id serial not null,
    manufacturer varchar(255),
    model varchar(255),
    serial_number varchar(255),
    primary key (id)
);

create table note (
    note_id serial not null,
    repair_id integer,
    visibility smallint check (visibility between 0 and 1),
    message varchar(255),
    primary key (note_id)
);

create table password_reset_token (
      user_id integer not null unique,
      expiry_date timestamp(6),
      id bigint not null,
      token varchar(255),
      primary key (id)
);

create table repair (
    device_id integer not null,
    id serial not null,
    issuer_user_id integer,
    primary key (id)
);

create table repair_costs (
    costs_id integer not null unique,
    repair_id integer not null
);

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
   primary key (user_id)
);

alter table if exists authorities
    add constraint FKk91upmbueyim93v469wj7b2qh foreign key (user_id) references users;
alter table if exists cost
    add constraint FKskp0e42nvl99cjhjof3fltbcg foreign key (repair_id) references repair;
alter table if exists note
    add constraint FK94ucwcm02pj66wdvxmnvahpqp foreign key (repair_id) references repair
        on delete cascade;
alter table if exists password_reset_token
    add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;
alter table if exists repair
    add constraint FKe2lm4qyk4g36lkdab4sqfxnwf foreign key (device_id) references device;
alter table if exists repair
    add constraint FKixsc0illvsi63j93mh26w003c
        foreign key (issuer_user_id) references users
            on delete cascade;
alter table if exists repair_costs
    add constraint FKtovovsvew76ohqhu2ytulwh0 foreign key (costs_id) references cost;
alter table if exists repair_costs
    add constraint FKmb1u5kjqqqrlb1pnmkw05xapc foreign key (repair_id) references repair;
alter table if exists users
    add constraint FKlbumi6chkcxaxk2m91y429dv1 foreign key (reset_token_id)
        references password_reset_token;
alter table if exists users
    add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id) references user_details;

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

insert into device(manufacturer, model, serial_number)
    values('test', 'test', 'test');

insert into repair (device_id, issuer_user_id)
    values (1, 1);

insert into note (repair_id, visibility, message)
    values(1, 0, 'test Note');

insert into note (repair_id, visibility, message)
values(1, 1, 'test Note');
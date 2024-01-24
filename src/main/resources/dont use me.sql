-- ### DROP DB ###
alter table if exists authorities drop constraint if exists FKk91upmbueyim93v469wj7b2qh;
alter table if exists cost drop constraint if exists FKskp0e42nvl99cjhjof3fltbcg;
alter table if exists note drop constraint if exists FKslvrt3e5dpkx0tm8wbtcxy093;
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
drop table if exists files cascade;
drop table if exists note cascade;
drop table if exists password_reset_token cascade;
drop table if exists repair cascade;
drop table if exists repair_costs cascade;
drop table if exists user_details cascade;
drop table if exists users cascade;
drop sequence if exists password_reset_token_seq;

-- ### CREATE DB ###
create sequence password_reset_token_seq start with 1 increment by 50;

create table files (
    id uuid not null,
    file_name varchar(255),
    file_type varchar(255),
    data oid,
    repair_id integer,
    primary key (id)
                   );

create table authorities (
    authority_id serial not null,
    user_id integer,
    authority varchar(255)
        check (authority in (
            'ROLE_CUSTOMER',
            'ROLE_EMPLOYEE',
            'ROLE_ADMIN')),
    username varchar(255),
    primary key (authority_id)
);

create table cost (
    cost_type smallint check (cost_type between 0 and 1),
    id serial not null,
    price float(53),
    repair_id integer not null,
    name varchar(255),
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
    author_id integer not null,
    note_id serial not null,
    repair_id integer not null,
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
    id serial not null,
    estimated_cost float(53),
    description varchar(255),
    repair_status varchar(255)
        check (repair_status in(
            'OPEN',
           'WAITING_FOR_CUSTOMER',
           'WAITING_FOR_SUPLIER',
           'CANCELED',
           'CLOSED')
            ),
    device_id integer,
    issuer_user_id integer,
    primary key (id));

create table repair_costs (
    costs_id integer not null unique,
    repair_id integer not null
);

create table if not exists user_details (
    user_details_id serial not null,
    first_name varchar(255),
    last_name varchar(255),
    phone_number varchar(255),
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
   email varchar(255) unique,
   password varchar(255),
   username varchar(255) unique,
   primary key (user_id)
);

alter table if exists authorities
    add constraint FKk91upmbueyim93v469wj7b2qh foreign key (user_id) references users;

alter table if exists cost
    add constraint FKskp0e42nvl99cjhjof3fltbcg foreign key (repair_id) references repair
    on delete cascade;

alter table if exists note
    add constraint FKslvrt3e5dpkx0tm8wbtcxy093 foreign key (author_id) references users;

alter table if exists note
    add constraint FK94ucwcm02pj66wdvxmnvahpqp foreign key (repair_id) references repair
        on delete cascade;

alter table if exists password_reset_token
    add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;

alter table if exists repair
    add constraint FKe2lm4qyk4g36lkdab4sqfxnwf foreign key (device_id) references device;

alter table if exists repair
    add constraint FKixsc0illvsi63j93mh26w003c foreign key (issuer_user_id) references users;

alter table if exists repair_costs
    add constraint FKtovovsvew76ohqhu2ytulwh0 foreign key (costs_id) references cost
    on delete cascade;

alter table if exists repair_costs
    add constraint FKmb1u5kjqqqrlb1pnmkw05xapc foreign key (repair_id) references repair;

alter table if exists users
    add constraint FKlbumi6chkcxaxk2m91y429dv1 foreign key (reset_token_id)
        references password_reset_token;

alter table if exists users
    add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id) references user_details;

insert into user_details (first_name, last_name, street, post_code, city)
    values ('Andrzej', 'Kowalski', 'Testowa 1', '00-000', 'Warszawa');

insert into users (username, password, active, email, user_detail_id)
     values ('root', '$2a$12$enZyMcOqcaSgS4oPxthFweOfCvY5zqkEHi1lwy7VZ2QWCfLF9P8/2', true, 'jakub_wilk@outlook.com', 1);

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_CUSTOMER');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_EMPLOYEE');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_ADMIN');

insert into device(manufacturer, model, serial_number)
    values('Lenovo', 'Thinkpad', 'L14GFR4');

insert into repair (device_id, issuer_user_id, estimated_cost, description, repair_status)
    values (1, 1, 400, 'Komputer nie włącza się po wciśnięciu przycisku', 'OPEN');

insert into repair (device_id, issuer_user_id, estimated_cost, description, repair_status)
    values (1, 1, 400, 'Komputer nie włącza się po wciśnięciu przycisku', 'OPEN');

insert into note (author_id, repair_id, visibility, message)
values(1, 1, 1, 'Rozpoczęto diagnostykę sprzętu');

insert into note (author_id, repair_id, visibility, message)
values(1, 1, 0, 'Podejrzewam uszkodzenie baterii');

insert into note (author_id, repair_id, visibility, message)
values(1, 2, 1, 'test Note');
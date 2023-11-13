create sequence password_reset_token_seq start with 1 increment by 50;
create table authorities (authority_id serial not null, user_id integer, authority varchar(255), username varchar(255), primary key (authority_id));
create table cost (cost_type smallint check (cost_type between 0 and 1), id serial not null, price float4, repair_id integer not null, primary key (id));
create table device (id serial not null, manufacturer varchar(255), model varchar(255), serial_number varchar(255), primary key (id));
create table note (id serial not null, repair_id integer, visibility smallint check (visibility between 0 and 1), message varchar(255), primary key (id));
create table password_reset_token (user_id integer not null unique, expiry_date timestamp(6), id bigint not null, token varchar(255), primary key (id));
create table repair (device_id integer not null, id serial not null, issuer_user_id integer unique, primary key (id));
create table repair_costs (costs_id integer not null unique, repair_id integer not null);
create table user_details (user_details_id serial not null, city varchar(255), first_name varchar(255), last_name varchar(255), post_code varchar(255), street varchar(255), primary key (user_details_id));
create table users (active boolean, user_detail_id integer not null unique, user_id serial not null, reset_token_id bigint unique, email varchar(255), password varchar(255), username varchar(255), primary key (user_id));
alter table if exists authorities add constraint FKk91upmbueyim93v469wj7b2qh foreign key (user_id) references users;
alter table if exists cost add constraint FKskp0e42nvl99cjhjof3fltbcg foreign key (repair_id) references repair;
alter table if exists note add constraint FK94ucwcm02pj66wdvxmnvahpqp foreign key (repair_id) references repair;
alter table if exists password_reset_token add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;
alter table if exists repair add constraint FKe2lm4qyk4g36lkdab4sqfxnwf foreign key (device_id) references device;
alter table if exists repair add constraint FKixsc0illvsi63j93mh26w003c foreign key (issuer_user_id) references users;
alter table if exists repair_costs add constraint FKtovovsvew76ohqhu2ytulwh0 foreign key (costs_id) references cost;
alter table if exists repair_costs add constraint FKmb1u5kjqqqrlb1pnmkw05xapc foreign key (repair_id) references repair;
alter table if exists users add constraint FKlbumi6chkcxaxk2m91y429dv1 foreign key (reset_token_id) references password_reset_token;
alter table if exists users add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id) references user_details;
create sequence password_reset_token_seq start with 1 increment by 50;
create table authorities (authority_id serial not null, user_id integer, authority varchar(255), username varchar(255), primary key (authority_id));
create table cost (id serial not null, price float4, repair_id integer not null, cost_type varchar(255) check (cost_type in ('PART','SERVICE')), primary key (id));
create table device (id serial not null, manufacturer varchar(255), model varchar(255), serial_number varchar(255), primary key (id));
create table note (id serial not null, repair_id integer, message varchar(255), visibility varchar(255) check (visibility in ('PUBLIC','PRIVATE')), primary key (id));
create table password_reset_token (user_id integer not null unique, expiry_date timestamp(6), id bigint not null, token varchar(255), primary key (id));
create table repair (device_id integer not null, id serial not null, issuer_user_id integer unique, repair_status varchar(255) check (repair_status in ('OPEN','WAITING_FOR_CUSTOMER','WAITING_FOR_SUPLIER','CANCELED','CLOSED')), primary key (id));
create table repair_costs (costs_id integer not null unique, repair_id integer not null);
create table user_details (user_details_id serial not null, city varchar(255), first_name varchar(255), last_name varchar(255), post_code varchar(255), street varchar(255), primary key (user_details_id));
create table users (active boolean, user_detail_id integer not null unique, user_id serial not null, reset_token_id bigint unique, email varchar(255), password varchar(255), username varchar(255), primary key (user_id));
alter table if exists authorities add constraint FKk91upmbueyim93v469wj7b2qh foreign key (user_id) references users;
alter table if exists cost add constraint FKskp0e42nvl99cjhjof3fltbcg foreign key (repair_id) references repair;
alter table if exists note add constraint FK94ucwcm02pj66wdvxmnvahpqp foreign key (repair_id) references repair;
alter table if exists password_reset_token add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;
alter table if exists repair add constraint FKe2lm4qyk4g36lkdab4sqfxnwf foreign key (device_id) references device;
alter table if exists repair add constraint FKixsc0illvsi63j93mh26w003c foreign key (issuer_user_id) references users;
alter table if exists repair_costs add constraint FKtovovsvew76ohqhu2ytulwh0 foreign key (costs_id) references cost;
alter table if exists repair_costs add constraint FKmb1u5kjqqqrlb1pnmkw05xapc foreign key (repair_id) references repair;
alter table if exists users add constraint FKlbumi6chkcxaxk2m91y429dv1 foreign key (reset_token_id) references password_reset_token;
alter table if exists users add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id) references user_details;
create sequence password_reset_token_seq start with 1 increment by 50;
create table authorities (authority_id serial not null, user_id integer, authority varchar(255), username varchar(255), primary key (authority_id));
create table cost (id serial not null, price float4, repair_id integer not null, cost_type varchar(255) check (cost_type in ('PART','SERVICE')), primary key (id));
create table device (id serial not null, manufacturer varchar(255), model varchar(255), serial_number varchar(255), primary key (id));
create table note (id serial not null, repair_id integer, message varchar(255), visibility varchar(255) check (visibility in ('PUBLIC','PRIVATE')), primary key (id));
create table password_reset_token (user_id integer not null unique, expiry_date timestamp(6), id bigint not null, token varchar(255), primary key (id));
create table repair (device_id integer not null, id serial not null, issuer_user_id integer unique, repair_status varchar(255) check (repair_status in ('OPEN','WAITING_FOR_CUSTOMER','WAITING_FOR_SUPLIER','CANCELED','CLOSED')), primary key (id));
create table repair_costs (costs_id integer not null unique, repair_id integer not null);
create table user_details (user_details_id serial not null, city varchar(255), first_name varchar(255), last_name varchar(255), post_code varchar(255), street varchar(255), primary key (user_details_id));
create table users (active boolean, user_detail_id integer not null unique, user_id serial not null, reset_token_id bigint unique, email varchar(255), password varchar(255), username varchar(255), primary key (user_id));
alter table if exists authorities add constraint FKk91upmbueyim93v469wj7b2qh foreign key (user_id) references users;
alter table if exists cost add constraint FKskp0e42nvl99cjhjof3fltbcg foreign key (repair_id) references repair;
alter table if exists note add constraint FK94ucwcm02pj66wdvxmnvahpqp foreign key (repair_id) references repair;
alter table if exists password_reset_token add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;
alter table if exists repair add constraint FKe2lm4qyk4g36lkdab4sqfxnwf foreign key (device_id) references device;
alter table if exists repair add constraint FKixsc0illvsi63j93mh26w003c foreign key (issuer_user_id) references users;
alter table if exists repair_costs add constraint FKtovovsvew76ohqhu2ytulwh0 foreign key (costs_id) references cost;
alter table if exists repair_costs add constraint FKmb1u5kjqqqrlb1pnmkw05xapc foreign key (repair_id) references repair;
alter table if exists users add constraint FKlbumi6chkcxaxk2m91y429dv1 foreign key (reset_token_id) references password_reset_token;
alter table if exists users add constraint FKfsi9283rk1akvfmlbfrhdn5vj foreign key (user_detail_id) references user_details;

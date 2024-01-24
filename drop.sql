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

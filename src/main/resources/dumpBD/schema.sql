create schema my_schema;

alter schema my_schema
  owner to postgres;

create sequence roles_id_seq
  as integer
  maxvalue 2147483647;

alter sequence roles_id_seq
  owner to postgres;

create sequence users_id_seq
  as integer
  maxvalue 2147483647;

alter sequence users_id_seq
  owner to postgres;

create table if not exists roles
(
  id          bigserial not null
    constraint roles_pkey
    primary key,
  name        varchar(50),
  description varchar(50)
);

alter table roles
  owner to postgres;

create unique index if not exists roles_name_uindex
  on roles (name);

create table if not exists users
(
  id         serial                              not null
    constraint users_pkey
    primary key,
  email      varchar(30)                         not null,
  password   varchar(100)                        not null,
  first_name varchar(10)                         not null,
  last_name  varchar(10)                         not null,
  phone      varchar(15)                         not null,
  birth_date date,
  gender     integer,
  created    timestamp default CURRENT_TIMESTAMP not null
);

alter table users
  owner to postgres;

create unique index if not exists users_email_uindex
  on users (email);

create table if not exists user_roles
(
  user_id integer not null
    constraint user_roles_users_id_fk
    references users,
  role_id integer not null
    constraint user_roles_roles_id_fk
    references roles
);

alter table user_roles
  owner to postgres;

create table if not exists friends
(
  user_id   bigint not null
    constraint friends_users_id_fk
    references users,
  friend_id bigint not null
    constraint friends_users_id_fk_2
    references users
);

alter table friends
  owner to postgres;


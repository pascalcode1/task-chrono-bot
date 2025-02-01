create table state
(
    id  integer not null primary key,
    name varchar(64)
);

create table users
(
    id                          bigserial   primary key,
    telegram_id                 bigint      unique,
    user_name                   varchar(128),
    first_name                  varchar(128),
    last_name                   varchar(128),
    min_week_hours              integer,
    first_day_of_week           integer,
    week_hours_stat             boolean     not null default false,
    state                       integer     not null default 0,
    meta                        varchar(128),
    constraint fk_state foreign key (state) references state (id)
);

create table task
(
    id                  bigserial       not null primary key,
    name                varchar(128)    not null,
    user_id             bigint          not null,
    static_task         boolean         default false,
    user_task_id        bigint          not null,
    constraint fk_user_id foreign key (user_id) references users (id),
    constraint unique_id_user_task_id unique(id, user_task_id),
    constraint unique_name_user_id unique(name, user_id)
);

create table task_log
(
    id      bigserial not null primary key,
    task_id bigint    not null,
    start   timestamp not null,
    stop    timestamp,
    constraint fk_task_id foreign key (task_id) references task (id)
);

insert into state (id, name) values (0, 'Home');
insert into state (id, name) values (1, 'Settings');
insert into state (id, name) values (2, 'Delete tasks');
insert into state (id, name) values (3, 'Pinned tasks');
insert into state (id, name) values (4, 'Edit tasks');
insert into state (id, name) values (5, 'Confirm editing');
insert into state (id, name) values (6, 'Edit time records list');
insert into state (id, name) values (7, 'Edit time record');
insert into state (id, name) values (8, 'Confirm time editing of time record');

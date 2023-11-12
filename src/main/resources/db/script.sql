create table users
(
    id                          bigserial   primary key,
    telegram_id                 bigint      unique,
    user_name                   varchar(128),
    first_name                  varchar(128),
    last_name                   varchar(128),
    rate                        integer,
    currency                    varchar(8),
    money_stat                  boolean     default false,
    min_week_hours              integer,
    first_day_of_week           integer,
    week_hours_stat             boolean     not null default false,
    add_new_tasks_to_button_bar boolean     not null default false,
    state                       integer     not null default 0,
    constraint fk_state foreign key (state) references state (id)
);

create table task
(
    id                  bigserial       not null primary key,
    name                varchar(128)    not null,
    user_id             bigint          not null,
    show_on_button_bar  boolean         default false,
    static_task         boolean         default false,
    constraint fk_user_id foreign key (user_id) references users (id)
);

create table task_log
(
    id      bigserial not null primary key,
    task_id bigint    not null,
    start   timestamp not null,
    stop    timestamp,
    constraint fk_task_id foreign key (task_id) references task (id)
);

create table state
(
    id  integer not null primary key,
    name varchar(32)
);
insert into state (id, name) values (0, 'Home');
insert into state (id, name) values (1, 'Settings');
insert into state (id, name) values (2, 'Delete');
insert into state (id, name) values (3, 'Static tasks');

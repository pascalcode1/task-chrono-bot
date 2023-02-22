create table users
(
    id                bigserial primary key,
    telegram_id       bigint unique,
    user_name         varchar(128),
    first_name        varchar(128),
    last_name         varchar(128),
    rate              integer,
    currency          varchar(8),
    money_stat        boolean,
    min_week_hours    integer,
    first_day_of_week integer,
    week_hours_stat   boolean,
    precontact_mode   boolean
);

create table task
(
    id       bigserial    not null primary key,
    name     varchar(128) not null,
    user_id  bigint       not null,
    archived boolean default false,
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

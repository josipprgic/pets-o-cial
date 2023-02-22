CREATE SCHEMA IF NOT EXISTS progi;

CREATE TYPE progi.user_type AS ENUM (
    'USER',
    'COMPANY'
);

CREATE TYPE progi.user_role AS ENUM (
    'USER',
    'ADMIN',
    'SUPER_ADMIN'
);

CREATE TABLE progi.user
(
    id             BIGSERIAL PRIMARY KEY,
    username       varchar(25) NOT NULL,
    password       varchar NOT NULL,
    first_name     varchar NOT NULL,
    last_name      varchar NOT NULL,
    email          varchar NOT NULL,
    user_type      progi.user_type NOT NULL,
    user_role      progi.user_role NOT NULL
);
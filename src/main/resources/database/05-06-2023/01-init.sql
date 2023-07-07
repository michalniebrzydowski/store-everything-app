--liquibase formatted sql
--changeset michal:1

CREATE TABLE users
(
    user_id  SERIAL PRIMARY KEY,
    name     VARCHAR(20)         NOT NULL,
    surname  VARCHAR(50)         NOT NULL,
    age      SMALLINT            NOT NULL,
    email    VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    enabled  BOOLEAN             NOT NULL
);

CREATE TABLE roles
(
    role_id SERIAL PRIMARY KEY,
    role    VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE users_roles
(
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

CREATE TABLE categories
(
    category_id SERIAL PRIMARY KEY,
    name        VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users_categories
(
    user_categories_id SERIAL PRIMARY KEY,
    user_id            BIGINT REFERENCES users,
    category_id        BIGINT references categories
);

CREATE TABLE information
(
    information_id SERIAL PRIMARY KEY,
    user_id        BIGINT REFERENCES users,
    category_id    BIGINT REFERENCES categories,
    title          VARCHAR(20)              NOT NULL,
    content        VARCHAR(500)             NOT NULL,
    link           VARCHAR(512),
    creation_date  TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE information_sharing
(
    sharing_id          SERIAL PRIMARY KEY,
    information_id      BIGINT REFERENCES information,
    shared_with_user_id BIGINT REFERENCES users,
    edit_permission     BOOLEAN DEFAULT FALSE
);

CREATE TABLE information_link_sharing
(
    link_sharing_id SERIAL PRIMARY KEY,
    information_id  BIGINT REFERENCES information,
    edit_permission BOOLEAN DEFAULT FALSE,
    share_link VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP WITH TIME ZONE
)

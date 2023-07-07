--liquibase formatted sql
--changeset michal:1

ALTER TABLE users
    ADD CONSTRAINT unique_login UNIQUE (login);
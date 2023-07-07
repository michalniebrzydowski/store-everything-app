--liquibase formatted sql
--changeset michal:1

ALTER TABLE users
    ADD COLUMN login VARCHAR(20) NOT NULL;
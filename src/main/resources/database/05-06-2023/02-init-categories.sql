--liquibase formatted sql
--changeset michal:1

INSERT INTO roles (role)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_LIMITED_USER');

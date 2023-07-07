--liquibase formatted sql
--changeset michal:1

ALTER TABLE information_link_sharing
DROP COLUMN expiration_date;

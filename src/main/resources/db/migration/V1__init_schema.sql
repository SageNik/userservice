CREATE SCHEMA IF NOT EXISTS main;

CREATE TABLE main.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    address VARCHAR(200),
    enabled BOOLEAN,
    role VARCHAR(50)
);

CREATE TABLE main.verification_code (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(100),
    expiry_date DATE,
    user_id BIGINT NOT NULL,
    expiration INTEGER
);
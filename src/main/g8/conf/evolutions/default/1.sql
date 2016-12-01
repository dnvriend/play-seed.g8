# Users schema

# --- !Ups

CREATE TABLE person (
    id SERIAL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL
);

# --- !Downs

DROP TABLE person;
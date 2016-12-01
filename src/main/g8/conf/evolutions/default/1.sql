# --- !Ups

CREATE TABLE person (
    id SERIAL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL
);

CREATE TABLE kafka_offsets (
  group_id VARCHAR(255) NOT NULL,
  topic VARCHAR(255) NOT NULL,
  partition BIGINT,
  topic_offset BIGINT
);

# --- !Downs

DROP TABLE person;

DROP TABLE kafka_offsets;
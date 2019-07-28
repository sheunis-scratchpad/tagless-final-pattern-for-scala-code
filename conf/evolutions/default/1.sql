# --- !Ups
CREATE TABLE people (
    id SERIAL,
    name character varying(50) NOT NULL,
    year smallint NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE IF EXISTS people CASCADE;

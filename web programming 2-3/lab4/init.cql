CREATE KEYSPACE IF NOT EXISTS web4 
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };

CREATE TABLE IF NOT EXISTS web4.points (
    id UUID PRIMARY KEY,
    x DOUBLE,
    y DOUBLE,
    r INT,
    in_area BOOLEAN
);

CREATE TABLE IF NOT EXISTS web4.users (
    username TEXT PRIMARY KEY,
    password TEXT
);
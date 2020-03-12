create table temperaturedata (
    DATE TIMESTAMP not null,
    VALUE FLOAT not null
);

ALTER TABLE temperaturedata ADD COLUMN id SERIAL PRIMARY KEY;
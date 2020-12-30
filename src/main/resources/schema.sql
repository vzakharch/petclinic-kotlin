CREATE TABLE pet_type (id SERIAL PRIMARY KEY, name VARCHAR(255));
CREATE TABLE speciality (id SERIAL PRIMARY KEY, name VARCHAR(255));
CREATE TABLE vet (id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255));
CREATE TABLE owner (id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255), address VARCHAR(255), city VARCHAR(255), telephone VARCHAR(255));
CREATE TABLE pet (id SERIAL PRIMARY KEY, birth_date DATE, name VARCHAR(255), type integer, owner integer);
CREATE TABLE visit (id SERIAL PRIMARY KEY, visit_date DATE, description VARCHAR(255), pet_id integer);
-- prepare and delete old db
SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'gatormail';
DROP DATABASE IF EXISTS gatormail;
DROP USER IF EXISTS gatormail;

-- create new db
CREATE ROLE gatormail WITH LOGIN PASSWORD 'changeme!';
CREATE DATABASE gatormail WITH OWNER gatormail ENCODING 'UTF8';

-- this will allow SELECT
GRANT SELECT ON ALL TABLES IN SCHEMA public TO gatormail;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO gatormail;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO gatormail;

-- this will allow CONNECT etc
GRANT ALL PRIVILEGES ON DATABASE gatormail TO gatormail;


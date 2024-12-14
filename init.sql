CREATE DATABASE IF NOT EXISTS superleaguedb;
USE superleaguedb;

GRANT ALL PRIVILEGES
ON superleaguedb.*
TO 'superleagueuser'@'%';
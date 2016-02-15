CREATE TABLE IF NOT EXISTS properties (
    id VARCHAR(40) NOT NULL PRIMARY KEY,
	login VARCHAR(50) NOT NULL,
	password VARCHAR(100),
	last_updated TIMESTAMP
);

insert into properties(id, login, password) values ('1', 'somas', 'xyz');

COMMIT;
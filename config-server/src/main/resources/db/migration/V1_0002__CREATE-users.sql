CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(40) NOT NULL PRIMARY KEY,
	username VARCHAR(40) NOT NULL,
	password VARCHAR(100),
	role VARCHAR(100),
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP
);

insert into users(id, username, password, role, created, updated) values
  ('1', 'somas', 'password', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

COMMIT;
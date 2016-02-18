CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(40) NOT NULL PRIMARY KEY,
	username VARCHAR(40) NOT NULL,
	password VARCHAR(100),
	role VARCHAR(100),
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP
);

insert into users(id, username, password, role, created, updated) values
  ('1', 'somas', '$2a$10$AYiOdARBWUpUx8aCsB7qSe5f424ufry1QmYZvC0jxRjAhsuWbvxMe', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); --password = password

COMMIT;
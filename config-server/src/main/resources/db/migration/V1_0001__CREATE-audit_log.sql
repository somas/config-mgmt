CREATE TABLE IF NOT EXISTS audit_log (
  id VARCHAR(40) NOT NULL PRIMARY KEY,
	user_id VARCHAR(40) NOT NULL,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
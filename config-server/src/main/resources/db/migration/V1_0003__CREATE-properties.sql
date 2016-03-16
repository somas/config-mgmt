CREATE TABLE IF NOT EXISTS properties (
  id VARCHAR(40) NOT NULL PRIMARY KEY,
	item_key VARCHAR(100),
	field_key VARCHAR(100),
	description TEXT,
  audit_id VARCHAR(40) references audit_log(id),
	version INTEGER,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT property_set UNIQUE (item_key, field_key, version)
);

insert into properties(id, item_key, field_key, description, version, created) values
  ('1', 'amazon', 'integration', '{"x":"y"}', 1, CURRENT_TIMESTAMP);

COMMIT;
CREATE TABLE IF NOT EXISTS properties (
  id VARCHAR(40) NOT NULL PRIMARY KEY,
	project_id VARCHAR(40) NOT NULL,
	item_key VARCHAR(100),
	field_key VARCHAR(100),
	description TEXT,
	version integer,
	last_updated TIMESTAMP,
  CONSTRAINT property_set UNIQUE (project_id, item_key, field_key, version)
);

insert into properties(id, project_id, item_key, field_key, description, version, last_updated) values
  ('1', '1', 'amazon', 'integration', '{x:y}', 1, LOCALTIMESTAMP);

COMMIT;
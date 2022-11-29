DROP SCHEMA PUBLIC CASCADE;
CREATE SCHEMA PUBLIC;

CREATE TABLE pool (
  id BIGSERIAL PRIMARY KEY,
  license VARCHAR(36) REFERENCES account(license),
  name VARCHAR(24) NOT NULL,
  built VARCHAR NOT NULL,
  volume INT NOT NULL,
  cost INT NOT NULL
);

CREATE TABLE measurement (
  id BIGSERIAL PRIMARY KEY,
  pool_id BIGINT REFERENCES pool(id),
  measured VARCHAR NOT NULL,
  temp INT NOT NULL,
  total_hardness INT NOT NULL,
  total_chlorine INT NOT NULL,
  total_bromine INT NOT NULL,
  free_chlorine INT NOT NULL,
  ph NUMERIC(2, 1) NOT NULL,
  total_alkalinity INT NOT NULL,
  cyanuric_acid INT NOT NULL
);

CREATE TABLE cleaning (
  id BIGSERIAL PRIMARY KEY,
  pool_id BIGINT REFERENCES pool(id),
  cleaned VARCHAR NOT NULL,
  brush BOOL NOT NULL,
  net BOOL NOT NULL,
  vacuum BOOL NOT NULL,
  skimmer_basket BOOL NOT NULL,
  pump_basket BOOL NOT NULL,
  pump_filter BOOL NOT NULL,
  deck BOOL NOT NULL
);

CREATE TABLE chemical (
  id BIGSERIAL PRIMARY KEY,
  pool_id BIGINT REFERENCES pool(id),
  added VARCHAR NOT NULL,
  chemical VARCHAR NOT NULL,
  amount NUMERIC(5, 2),
  unit VARCHAR NOT NULL
);

CREATE TABLE fault (
  cause VARCHAR NOT NULL,
  ocurred VARCHAR NOT NULL,
  PRIMARY KEY (ocurred)
);
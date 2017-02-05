/***********************************************************************************************************************
  File: V1486288010__configs_create_client.sql

  Desc: Create the client table in the configs database and load it with seed data.  The table creation and
  seed data are intended to support integration tests.  The schema and seed data are representative of what is deployed
  when a TDS system is deployed.

***********************************************************************************************************************/
USE configs;

DROP TABLE IF EXISTS client;

CREATE TABLE client (
  name varchar(100) NOT NULL,
  origin varchar(50) DEFAULT NULL,
  internationalize bit(1) NOT NULL DEFAULT b'1',
  defaultlanguage varchar(50) NOT NULL DEFAULT 'enu',
  PRIMARY KEY (name)
);

INSERT INTO client VALUES ('SBAC_PT', NULL, 1, 'ENU');
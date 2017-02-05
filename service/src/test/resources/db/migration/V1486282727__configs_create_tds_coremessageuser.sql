/***********************************************************************************************************************
  File: V1486282727__configs_create_tds_coremessageuser.sql

  Desc: Create the tds_coremessageuser table in the configs database and load it with seed data.  The table creation and
  seed data are intended to support integration tests.  The schema and seed data are representative of what is deployed
  when a TDS system is deployed.

***********************************************************************************************************************/
USE configs;

DROP TABLE IF EXISTS tds_coremessageuser;

CREATE TABLE tds_coremessageuser (
  _fk_coremessageobject bigint(20) NOT NULL,
  systemid varchar(100) NOT NULL,
  PRIMARY KEY (_fk_coremessageobject,systemid)
);

INSERT INTO tds_coremessageuser VALUES (3146, 'Database');
/***********************************************************************************************************************
  File: V1472923782__session_create_table__externs.sql

  Desc: Create the _externs table in the session database and load it with seed data.  The table creation and seed data
  are intended to support integration tests.  The schema and seed data are representative of what is deployed when a TDS
  system is deployed.

  The query to fetch client system flag information from the configs database relies on the session.externs view, which
  relies on this table being present (as well as several others).  This means that there are some instances where the
  Config service will query data from the session database.

***********************************************************************************************************************/
USE session;
DROP TABLE IF EXISTS _externs;
CREATE TABLE _externs (
  clientname varchar(100) NOT NULL,
  environment varchar(50) NOT NULL,
  shiftwindowstart int(11) NOT NULL DEFAULT 0,
  shiftwindowend int(11) NOT NULL DEFAULT 0,
  shiftformstart int(11) NOT NULL DEFAULT 0,
  shiftformend int(11) NOT NULL DEFAULT 0,
  shiftftstart int(11) NOT NULL DEFAULT 0,
  shiftftend int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (clientname)
);
INSERT INTO _externs VALUES ('SBAC','Development',0,0,0,0,0,0);
INSERT INTO _externs VALUES ('SBAC_PT','Development',0,0,0,0,0,0);
/***********************************************************************************************************************
  File: V1486283158__configs_create_tds_coremessageobject.sql

  Desc: Create the tds_coremessageobject table in the configs database and load it with seed data.  The table creation and
  seed data are intended to support integration tests.  The schema and seed data are representative of what is deployed
  when a TDS system is deployed.

***********************************************************************************************************************/
USE configs;

DROP TABLE IF EXISTS tds_coremessageobject;

CREATE TABLE tds_coremessageobject (
  context varchar(100) NOT NULL,
  contexttype varchar(50) NOT NULL,
  messageid int(11) NOT NULL,
  ownerapp varchar(100) NOT NULL,
  appkey varchar(255) NOT NULL,
  message text,
  paralabels varchar(255) DEFAULT NULL,
  _key bigint(20) NOT NULL,
  fromclient varchar(100) DEFAULT NULL,
  datealtered datetime(3) DEFAULT NULL,
  nodes text,
  ismessageshowtouser bit(1) DEFAULT NULL,
  PRIMARY KEY (_key),
  UNIQUE KEY ix_uniqueappmsg (ownerapp,messageid,contexttype,context),
  KEY ix_coremsgcontext (context)
);

/***********************************************************************************************************************
  File: V1486283476__configs_create_client_messagetranslation.sql

  Desc: Create the client_messagetranslation table in the configs database and load it with seed data.  The table creation and
  seed data are intended to support integration tests.  The schema and seed data are representative of what is deployed
  when a TDS system is deployed.

***********************************************************************************************************************/
USE configs;

DROP TABLE IF EXISTS client_messagetranslation;

CREATE TABLE client_messagetranslation (
  _fk_coremessageobject bigint(20) NOT NULL,
  client varchar(100) NOT NULL,
  message text NOT NULL,
  language varchar(30) NOT NULL,
  grade varchar(25) NOT NULL DEFAULT '--any--',
  subject varchar(50) NOT NULL DEFAULT '--any--',
  _key varbinary(16) NOT NULL,
  datealtered datetime(3) DEFAULT NULL,
  PRIMARY KEY (_key),
  KEY ix_msgtrans (_fk_coremessageobject,client,language)
);

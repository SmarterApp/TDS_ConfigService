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

INSERT INTO client_messagetranslation VALUES (3146, 'SBAC_PT', 'Ao hiki iā oe ke hana i kēia hōike a hiki i ka lā  {0}.', 'HAW', '--ANY--', '--ANY--', 0xB32C36BFDF094559A65E46DBDD5DF602, '2013-08-01 11:02:31');
INSERT INTO client_messagetranslation VALUES (3146, 'SBAC_PT', 'No puede realizar esta prueba hasta {0}.', 'ESN', '--ANY--', '--ANY--', 0x46F548F386ED4A92B554C6FCC835C2AF, '2013-08-01 11:02:31');
INSERT INTO client_messagetranslation VALUES (3146, 'SBAC', 'Ao hiki iā oe ke hana i kēia hōike a hiki i ka lā  {0}.', 'HAW', '--ANY--', '--ANY--', 0xC43A1E73EB7E11E68FF40A73DE62EC37, '2013-08-02 10:34:37');

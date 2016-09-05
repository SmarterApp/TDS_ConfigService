/***********************************************************************************************************************
  File: V1472923731__configs_create_table_geo_clientapplication.sql

  Desc: Create the geo_clientapplication table in the configs database and load it with seed data.  The table creation
  and seed data are intended to support integration tests.  When a TDS system is deployed this table has no data, thus
  no seed data is included.

  This table does not directly affect any of the integration tests, the session.externs view references this table.  In
  order to have an accurate schema to test against, this table is created.  This table has a foreign key constraint that
  references the geo_database table, thus that table must also be created.

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE configs;
DROP TABLE IF EXISTS geo_clientapplication;
CREATE TABLE geo_clientapplication (
  clientname varchar(100) NOT NULL,
  environment varchar(100) NOT NULL,
  url varchar(200) DEFAULT NULL,
  appname varchar(100) NOT NULL,
  servicetype varchar(50) NOT NULL,
  _fk_geo_database varbinary(16) NOT NULL,
  _key varbinary(16) NOT NULL,
  isactive bit(1) DEFAULT NULL,
  PRIMARY KEY (_key),
  KEY fk_geoapp_db (_fk_geo_database),
  KEY ix_geoclientapp (clientname,environment,appname,servicetype),
  CONSTRAINT fk_geoapp_db FOREIGN KEY (_fk_geo_database) REFERENCES geo_database (_key) ON DELETE NO ACTION ON UPDATE NO ACTION
);
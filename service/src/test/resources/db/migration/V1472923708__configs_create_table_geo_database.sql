/***********************************************************************************************************************
  File: V1472923708__configs_create_table_geo_database.sql

  Desc: Create the geo_database table in the configs database and load it with seed data.  The table creation and seed
  data are intended to support integration tests.  When a TDS system is deployed this table has no data, thus no seed
  data is included.

  This table does not directly affect any of the integration tests; the geo_clientapplications table has a foreign key
  constraint that references this table.  In order to have an accurate schema to test against, this table is created.

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE configs;
DROP TABLE IF EXISTS geo_database;
CREATE TABLE geo_database (
  servername varchar(100) NOT NULL,
  dbname varchar(100) NOT NULL,
  brokerguid varbinary(16) DEFAULT NULL,
  _key varbinary(16) NOT NULL,
  tds_id varchar(25) DEFAULT NULL,
  PRIMARY KEY (_key)
);

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

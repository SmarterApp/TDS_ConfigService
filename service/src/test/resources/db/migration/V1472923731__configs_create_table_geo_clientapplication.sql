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
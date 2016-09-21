/***********************************************************************************************************************
  File: V1473808129__configs_create_table_client_timelimits.sql

  Desc: Create the client_timelimits table in the session database and load it with seed data.  The table creation and
  seed data are intended to support integration tests.  The schema and seed data are representative of what is deployed
  when a TDS system is deployed.

  The query to fetch client time limit information from the configs database relies on the session._externs table, which
  relies on this table being present.  This means that there are some instances where the Config service will query data
  from the session database.

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE configs;

DROP TABLE IF EXISTS client_timelimits;

CREATE TABLE client_timelimits (
  _key varbinary(16) NOT NULL,
  _efk_testid varchar(255) DEFAULT NULL,
  oppexpire int(11) NOT NULL,
  opprestart int(11) NOT NULL,
  oppdelay int(11) NOT NULL,
  interfacetimeout int(11) DEFAULT NULL,
  clientname varchar(100) DEFAULT NULL,
  ispracticetest bit(1) NOT NULL DEFAULT 0,
  refreshvalue int(11) DEFAULT NULL,
  tainterfacetimeout int(11) DEFAULT NULL,
  tacheckintime int(11) DEFAULT NULL,
  datechanged datetime(3) DEFAULT NULL,
  datepublished datetime(3) DEFAULT NULL,
  environment varchar(100) DEFAULT NULL,
  sessionexpire int(11) NOT NULL DEFAULT 8,
  requestinterfacetimeout int(11) NOT NULL DEFAULT 120,
  refreshvaluemultiplier int(11) NOT NULL DEFAULT 2,
  PRIMARY KEY (_key),
  KEY ix_clienttimelimits (clientname,_efk_testid)
);
INSERT INTO client_timelimits VALUES (X'0AEFBFBD6C362DEFBFBD49EFBFBDEFBF',NULL,1,10,-1,10,'SBAC_PT',1,30,20,20,'2012-12-21 00:02:53.000','2012-12-21 00:02:53.000',NULL,8,15,2);
INSERT INTO client_timelimits VALUES (X'C3AD5010256F4F2AA68653A0CF56CF55','SBAC Math 3-MATH-3',1,10,-1,10,'SBAC_PT',1,30,20,20,'2012-12-21 00:02:53.000','2012-12-21 00:02:53.000',NULL,8,15,2);
INSERT INTO client_timelimits VALUES (X'0B6CEFBFBDEFBFBD6D064042EFBFBD7D',NULL,1,10,-1,15,'SBAC',0,20,20,20,'2010-07-07 15:39:31.433',NULL,NULL,8,120,2);

/***********************************************************************************************************************
  File: V1475356973__configs_create_client_test_mode_and_window.sql

  Desc: Creates the client test mode and client test window tables and inserts some seed data for tests.

***********************************************************************************************************************/

USE configs;

DROP TABLE IF EXISTS client_testmode;
CREATE TABLE client_testmode (
  clientname varchar(100) NOT NULL,
  testid varchar(200) NOT NULL,
  mode varchar(50) NOT NULL DEFAULT 'online',
  algorithm varchar(50) DEFAULT NULL,
  formtideselectable bit(1) NOT NULL DEFAULT 0,
  issegmented bit(1) NOT NULL DEFAULT 0,
  maxopps int(11) NOT NULL DEFAULT 50,
  requirertsform bit(1) NOT NULL DEFAULT 0,
  requirertsformwindow bit(1) NOT NULL DEFAULT 0,
  requirertsformifexists bit(1) NOT NULL DEFAULT 1,
  sessiontype int(11) NOT NULL DEFAULT '-1',
  testkey varchar(250) DEFAULT NULL,
  _key varbinary(16) NOT NULL,
  PRIMARY KEY (_key),
  UNIQUE KEY ix_clienttestmode (testkey,sessiontype),
  KEY ix_testmode (clientname,testid,sessiontype),
  KEY ix_testmodekey (testkey),
  KEY ix_testmode_test (testid)
);

INSERT INTO client_testmode VALUES
  ('SBAC_PT','SBAC-Mathematics-11','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',X'0431F6515F2D11E6B2C80243FCF25EAB'),
  ('SBAC_PT','SBAC-IRP-CAT-ELA-11','online','adaptive2',0,0,999,0,0,1,0,'(SBAC_PT)SBAC-IRP-CAT-ELA-11-Summer-2015-2016',X'05770493221A11E6B4CC0243FCF25EAB'),
  ('SBAC_PT','SBAC-Mathematics-6','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-6-Spring-2013-2015',X'09E7BAEA5F2D11E6B2C80243FCF25EAB');

DROP TABLE IF EXISTS client_testwindow;
CREATE TABLE client_testwindow (
  clientname varchar(100) NOT NULL,
  testid varchar(200) NOT NULL,
  window int(11) NOT NULL DEFAULT 1,
  numopps int(11) NOT NULL,
  startdate datetime(3) DEFAULT NULL,
  enddate datetime(3) DEFAULT NULL,
  origin varchar(100) DEFAULT NULL,
  source varchar(100) DEFAULT NULL,
  windowid varchar(50) DEFAULT NULL,
  _key varbinary(16) NOT NULL,
  sessiontype int(11) NOT NULL DEFAULT -1,
  sortorder int(11) DEFAULT 1,
  PRIMARY KEY (_key),
  KEY fk_timewindow (clientname,windowid),
  KEY ix_clienttestwindow (clientname,testid),
  KEY ix_testwindow_test (testid)
);

INSERT INTO client_testwindow VALUES
  ('SBAC_PT','SBAC-Mathematics-11',1,3,'2016-08-10 19:02:43.000','2017-08-10 19:02:43.000',NULL,NULL,'ANNUAL',X'043A37525F2D11E6B2C80243FCF25EAB',-1,1),
  ('SBAC_PT','SBAC-IRP-CAT-ELA-11',1,999,'2016-05-25 01:43:03.000','2017-05-25 01:43:03.000',NULL,NULL,'ANNUAL',X'05778300221A11E6B4CC0243FCF25EAB',-1,1),
  ('SBAC_PT','SBAC-Mathematics-6',1,3,'2016-08-10 19:02:52.000','2017-08-10 19:02:52.000',NULL,NULL,'ANNUAL',X'09F1C11C5F2D11E6B2C80243FCF25EAB',-1,1);
/***********************************************************************************************************************
  File: V1472923684__configs_create_table_client_testproperties.sql

  Desc: Create the client_testproperties table in the configs database and load it with seed data.  The table creation
  and seed data are intended to support integration tests.  The schema and seed data are representative of what is
  deployed when a TDS system is deployed.

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE configs;
DROP TABLE IF EXISTS client_testproperties;
CREATE TABLE client_testproperties (
  clientname varchar(100) NOT NULL,
  testid varchar(255) NOT NULL,
  maxopportunities int(11) DEFAULT NULL,
  handscoreproject int(11) DEFAULT NULL,
  prefetch int(11) NOT NULL DEFAULT '2',
  datechanged datetime(3) DEFAULT NULL,
  isprintable bit(1) NOT NULL DEFAULT 0,
  isselectable bit(1) NOT NULL DEFAULT 1,
  label varchar(255) DEFAULT NULL,
  printitemtypes varchar(1000) DEFAULT '',
  scorebytds bit(1) NOT NULL DEFAULT 1,
  batchmodereport bit(1) NOT NULL DEFAULT 0,
  subjectname varchar(100) DEFAULT NULL,
  origin varchar(50) DEFAULT NULL,
  source varchar(50) DEFAULT NULL,
  maskitemsbysubject bit(1) NOT NULL DEFAULT 1,
  initialabilitybysubject bit(1) NOT NULL DEFAULT 1,
  startdate datetime(3) DEFAULT NULL,
  enddate datetime(3) DEFAULT NULL,
  ftstartdate datetime(3) DEFAULT NULL,
  ftenddate datetime(3) DEFAULT NULL,
  accommodationfamily varchar(50) DEFAULT NULL,
  sortorder int(11) DEFAULT NULL,
  rtsformfield varchar(50) NOT NULL DEFAULT 'tds-testform',
  rtswindowfield varchar(50) NOT NULL DEFAULT 'tds-testwindow',
  windowtideselectable bit(1) NOT NULL DEFAULT 0,
  requirertswindow bit(1) NOT NULL DEFAULT 0,
  reportinginstrument varchar(50) DEFAULT NULL,
  tide_id varchar(100) DEFAULT NULL,
  forcecomplete bit(1) NOT NULL DEFAULT 1,
  rtsmodefield varchar(50) NOT NULL DEFAULT 'tds-testmode',
  modetideselectable bit(1) NOT NULL DEFAULT 0,
  requirertsmode bit(1) NOT NULL DEFAULT 0,
  requirertsmodewindow bit(1) NOT NULL DEFAULT 0,
  deleteunanswereditems bit(1) NOT NULL DEFAULT 0,
  abilityslope float NOT NULL DEFAULT '1',
  abilityintercept float NOT NULL DEFAULT '0',
  validatecompleteness bit(1) NOT NULL DEFAULT 0,
  gradetext varchar(50) DEFAULT NULL,
  initialabilitytestid varchar(100) DEFAULT NULL,
  proctoreligibility int(11) NOT NULL DEFAULT '0',
  category varchar(50) DEFAULT NULL,
  CONSTRAINT pk_client_testproperties PRIMARY KEY (clientname,testid),
  KEY ix_testprops_test (testid)
);

INSERT INTO client_testproperties VALUES ('SBAC_PT','IRP-Perf-ELA-11',999,NULL,2,NULL,0,1,'Grade 11 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','IRP-Perf-ELA-3',999,NULL,2,NULL,0,1,'IRP-Perf-ELA-3','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','IRP-Perf-ELA-7',999,NULL,2,NULL,0,1,'Grade 7 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC ELA 3-ELA-3',3,NULL,2,NULL,0,1,'Grades 3 - 5 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 3 - 5',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC ELA 6-ELA-6',3,NULL,2,NULL,0,1,'Grades 6 - 8 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 6 - 8',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC ELA HS-ELA-10',3,NULL,2,NULL,0,1,'High School ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'High School',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC Math 3-MATH-3',3,NULL,2,NULL,0,1,'Grades 3 - 5 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 3 - 5',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC Math 6-MATH-6',3,NULL,2,NULL,0,1,'Grade 6 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 6',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC Math 7-MATH-7',3,NULL,2,NULL,0,1,'Grades 7 - 8 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 7 - 8',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC Math HS-MATH-10',3,NULL,2,NULL,0,1,'High School MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'High School',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-11',3,NULL,2,NULL,0,1,'Grade 11 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-3',3,NULL,2,NULL,0,1,'Grade 3 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-4',3,NULL,2,NULL,0,1,'Grade 4 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 4',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-5',3,NULL,2,NULL,0,1,'Grade 5 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 5',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-6',3,NULL,2,NULL,0,1,'Grade 6 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 6',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-7',3,NULL,2,NULL,0,1,'Grade 7 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-ELA-8',3,NULL,2,NULL,0,1,'Grade 8 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 8',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-CAT-ELA-11',999,NULL,2,NULL,0,1,'IRP Grade 11 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-CAT-ELA-3',999,NULL,2,NULL,0,1,'IRP Grade 3 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-CAT-ELA-7',999,NULL,2,NULL,0,1,'IRP Grade 7 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-CAT-MATH-11',999,NULL,2,NULL,0,1,'IRP Grade 11 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-CAT-MATH-3',999,NULL,2,NULL,0,1,'IRP Grade 3 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-Mathematics-7',999,NULL,2,NULL,0,1,'IRP Grade 7 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-Perf-MATH-11',999,NULL,2,NULL,0,1,'IRP Perf Grade 11 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-Perf-MATH-3',999,NULL,2,NULL,0,1,'IRP Perf Grade 3 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-IRP-Perf-MATH-7',999,NULL,2,NULL,0,1,'IRP Perf Grade 7 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-11',3,NULL,2,NULL,0,1,'Grade 11 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-3',3,NULL,2,NULL,0,1,'Grade 3 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-4',3,NULL,2,NULL,0,1,'Grade 4 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 4',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-5',3,NULL,2,NULL,0,1,'Grade 5 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 5',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-6',3,NULL,2,NULL,0,1,'Grade 6 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 6',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-7',3,NULL,2,NULL,0,1,'Grade 7 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Mathematics-8',3,NULL,2,NULL,0,1,'Grade 8 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 8',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-11',3,NULL,2,NULL,0,1,'Perf Grade 11 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-3',9999,NULL,2,NULL,0,1,'Perf Grade 3 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-4',3,NULL,2,NULL,0,1,'Perf Grade 4 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 4',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-5',3,NULL,2,NULL,0,1,'Perf Grade 5 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 5',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-6',3,NULL,2,NULL,0,1,'Perf Grade 6 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 6',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-7',3,NULL,2,NULL,0,1,'Perf Grade 7 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-ELA-8',3,NULL,2,NULL,0,1,'Perf Grade 8 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 8',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-11',3,NULL,2,NULL,0,1,'Perf Grade 11 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-3',3,NULL,2,NULL,0,1,'Perf Grade 3 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 3',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-4',3,NULL,2,NULL,0,1,'Perf Grade 4 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 4',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-5',3,NULL,2,NULL,0,1,'Perf Grade 5 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 5',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-6',3,NULL,2,NULL,0,1,'Perf Grade 6 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 6',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-7',3,NULL,2,NULL,0,1,'Perf Grade 7 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 7',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Perf-MATH-8',3,NULL,2,NULL,0,1,'Perf Grade 8 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 8',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-SH-IRP-Student Help-11',999,NULL,2,NULL,0,0,'SBAC-SH-IRP-Student Help-11','',0,0,'Student Help',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'Student Help',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 3 - 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBAC-Student Help-11',3,NULL,2,NULL,0,0,'SBAC-Student Help-11','',0,0,'Student Help',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'Student Help',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 3 - 11',NULL,0,NULL);
INSERT INTO client_testproperties VALUES ('SBAC_PT','SBACTraining-Student Help-11',3,NULL,2,NULL,0,0,'SBACTraining-Student Help-11','',0,0,'Student Help',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'Student Help',NULL,'tds-testform','tds-testwindow',0,0,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grades 3 - 11',NULL,0,NULL);
/***********************************************************************************************************************
  File: V1472923547__configs_create_table_client_systemflags.sql

  Desc: Create the client_systemflags table in the configs database and load it with seed data.  The table creation and
  seed data are intended to support integration tests.  The schema and seed data are representative of what is deployed
  when a TDS system is deployed.

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE configs;
DROP TABLE IF EXISTS client_systemflags;
CREATE TABLE client_systemflags (
  auditobject varchar(255) NOT NULL,
  ison int(11) NOT NULL,
  description varchar(255) DEFAULT NULL,
  clientname varchar(100) NOT NULL,
  ispracticetest bit(1) NOT NULL,
  datechanged datetime(3) DEFAULT NULL,
  datepublished datetime(3) DEFAULT NULL,
  PRIMARY KEY (clientname,auditobject,ispracticetest)
);

INSERT INTO client_systemflags VALUES ('accommodations',1,'keeps an audit trail of various changes to accommodations settings','SBAC',0,'2011-06-01 11:27:47.980',NULL);
INSERT INTO client_systemflags VALUES ('AnonymousTestee',1,'Permits anonymous login by testees (for practice test)','SBAC',0,'2011-05-24 15:16:32.757',NULL);
INSERT INTO client_systemflags VALUES ('CertifyProctor',0,'TRAINEDPROCTOR','SBAC',0,'2010-06-22 11:55:43.967',NULL);
INSERT INTO client_systemflags VALUES ('items',0,'Toggles audit trail on item selection','SBAC',0,'2010-06-22 11:55:43.967',NULL);
INSERT INTO client_systemflags VALUES ('latencies',1,'Toggles audit trail on system latencies','SBAC',0,'2011-05-24 15:16:32.757',NULL);
INSERT INTO client_systemflags VALUES ('MatchTesteeProctorSchool',1,'INSTITUTION','SBAC',0,'2010-08-16 10:21:08.193',NULL);
INSERT INTO client_systemflags VALUES ('opportunities',1,'Toggles audit trail on opportunities','SBAC',0,'2011-05-24 15:16:32.757',NULL);
INSERT INTO client_systemflags VALUES ('oppreport',1,'Allows/disallows xml reporting of opportunities','SBAC',0,'2011-05-24 15:49:06.323',NULL);
INSERT INTO client_systemflags VALUES ('ProctorActivity',1,'Monitor proctor activity','SBAC',0,'2010-09-22 11:51:03.070',NULL);
INSERT INTO client_systemflags VALUES ('proctorless',1,'Permits login to proctorless session','SBAC',0,'2010-09-22 11:51:03.070',NULL);
INSERT INTO client_systemflags VALUES ('ProctorTraining',0,'Allows proctor app to differentiate between operational system and training system','SBAC',0,'2010-09-22 11:51:03.073',NULL);
INSERT INTO client_systemflags VALUES ('responses',1,'Toggles audit trail on testee responses','SBAC',0,'2010-09-22 11:51:03.073',NULL);
INSERT INTO client_systemflags VALUES ('RestoreAccommodations',1,'Restore RTS Accommodation values on test resume','SBAC',0,'2010-09-22 11:51:03.077',NULL);
INSERT INTO client_systemflags VALUES ('scores',1,'Toggles audit trail on opportunity scores','SBAC',0,'2010-09-22 11:51:03.077',NULL);
INSERT INTO client_systemflags VALUES ('sessions',1,'Toggles audit trail on sessions','SBAC',0,'2010-09-22 11:51:03.077',NULL);
INSERT INTO client_systemflags VALUES ('SuppressScores',0,'Keeps proctors from seeing test scores','SBAC',0,'2010-08-17 08:46:58.597',NULL);
INSERT INTO client_systemflags VALUES ('accommodations',1,'keeps an audit trail of various changes to accommodations settings','SBAC_PT',1,'2011-06-01 11:27:47.980',NULL);
INSERT INTO client_systemflags VALUES ('AnonymousTestee',1,'Permits anonymous login by testees (for practice test)','SBAC_PT',1,'2011-05-24 15:16:32.757',NULL);
INSERT INTO client_systemflags VALUES ('CertifyProctor',0,'TRAINEDPROCTOR','SBAC_PT',1,'2010-06-22 11:55:43.967',NULL);
INSERT INTO client_systemflags VALUES ('items',0,'Toggles audit trail on item selection','SBAC_PT',1,'2010-06-22 11:55:43.967',NULL);
INSERT INTO client_systemflags VALUES ('latencies',1,'Toggles audit trail on system latencies','SBAC_PT',1,'2011-05-24 15:16:32.757',NULL);
INSERT INTO client_systemflags VALUES ('MatchTesteeProctorSchool',0,'INSTITUTION','SBAC_PT',1,'2010-08-16 10:21:08.193',NULL);
INSERT INTO client_systemflags VALUES ('opportunities',1,'Toggles audit trail on opportunities','SBAC_PT',1,'2011-05-24 15:16:32.757',NULL);
INSERT INTO client_systemflags VALUES ('oppreport',1,'Allows/disallows xml reporting of opportunities','SBAC_PT',1,'2011-05-24 15:49:06.323',NULL);
INSERT INTO client_systemflags VALUES ('ProctorActivity',1,'Monitor proctor activity','SBAC_PT',1,'2010-09-22 11:51:03.070',NULL);
INSERT INTO client_systemflags VALUES ('proctorless',1,'Permits login to proctorless session','SBAC_PT',1,'2010-09-22 11:51:03.070',NULL);
INSERT INTO client_systemflags VALUES ('ProctorTraining',1,'Allows proctor app to differentiate between operational system and training system','SBAC_PT',1,'2010-09-22 11:51:03.073',NULL);
INSERT INTO client_systemflags VALUES ('responses',1,'Toggles audit trail on testee responses','SBAC_PT',1,'2010-09-22 11:51:03.073',NULL);
INSERT INTO client_systemflags VALUES ('RestoreAccommodations',1,'Restore RTS Accommodation values on test resume','SBAC_PT',1,'2010-09-22 11:51:03.077',NULL);
INSERT INTO client_systemflags VALUES ('scores',1,'Toggles audit trail on opportunity scores','SBAC_PT',1,'2010-09-22 11:51:03.077',NULL);
INSERT INTO client_systemflags VALUES ('sessions',1,'Toggles audit trail on sessions','SBAC_PT',1,'2010-09-22 11:51:03.077',NULL);
INSERT INTO client_systemflags VALUES ('SuppressScores',0,'Keeps proctors from seeing test scores','SBAC_PT',1,'2010-08-17 08:46:58.597',NULL);
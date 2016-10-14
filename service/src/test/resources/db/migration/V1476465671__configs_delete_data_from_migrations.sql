/***********************************************************************************************************************
  File: V1476465671__configs_delete_data_from_migrations.sql

  Desc: Deletes all the data that is not lookup table data.  For example, specific configuration data for specific
  assessments are deleted.  Instead of having migrations populate these tables we will do it in tests.

***********************************************************************************************************************/
use configs;

delete from client_systemflags;
delete from client_testproperties;
delete from client_externs;
delete from client_timelimits;
delete from client_testmode;
delete from client_testwindow;
delete from client_testformproperties;
delete from client_segmentproperties;
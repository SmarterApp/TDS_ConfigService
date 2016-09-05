/***********************************************************************************************************************
  File: beforeMigrate.sql

  Desc: Create the configs and session schemas that the Config service depends on.  This script will be executed as a
  part of Flyway's run cycle before any of the migrations are executed.  For more details on how to hook into Flyway,
  check here:  https://flywaydb.org/documentation/callbacks

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
CREATE SCHEMA IF NOT EXISTS configs;
CREATE SCHEMA IF NOT EXISTS session;
/***********************************************************************************************************************
  File: V1476563594__configs_drop_unused_session_view_tables.sql

  Desc: Drops the session _externs table and externs view since no longer used

***********************************************************************************************************************/

use session;

DROP TABLE IF EXISTS _externs;
DROP VIEW IF EXISTS externs;
DROP VIEW IF EXISTS timelimits;

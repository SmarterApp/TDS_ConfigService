/***********************************************************************************************************************
  File: V1472923813__session_create_view_externs.sql

  Desc: Create the timelimits view in the session database and load it with seed data.  The table creation
  and seed data are intended to support integration tests.  The schema and seed data are representative of what is
  deployed when a TDS system is deployed.

  The query to fetch time limit information from the configs.client_timelimits database relies on the session._externs
  table, which relies on this table being present (as well as several others).  This means that there are some instances
  where the Config service will query data from the session database.

  NOTE: Is this view required?  The only data collected from the session database is the session._externs.clientname and
  session._externs.environment values.  The configs.client_externs table has the same values with the same data:

    SELECT c._key, c.clientname, c.environment FROM configs.client_externs c;
    # _key, clientname, environment
    ?, 'SBAC', 'Development'
    ?, 'SBAC_PT', 'Development'

  SELECT clientName, environment FROM session._externs; -- this is the TABLE, not the VIEW.  The "foo", "bar" record was
  inserted by me at some point during troubleshooting the deployment process.

    # clientName, environment
    'foo', 'bar'
    'SBAC', 'Development'
    'SBAC_PT', 'Development'

  The configs.client_timelimits table also has columns to store clientname and environment, but the values in those
  columns appear to be different than the configs.client_externs and session._externs tables:

    SELECT clientname, _efk_testid, environment FROM configs.client_timelimits;

    # clientname, _efk_testid, environment
    'SBAC_PT', NULL, NULL
    'SBAC', NULL, NULL

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE session;
CREATE OR REPLACE VIEW timelimits AS
  SELECT 
    l._efk_testid AS _efk_testid,
    l.oppexpire AS oppexpire,
    l.opprestart AS opprestart,
    l.oppdelay AS oppdelay,
    l.interfacetimeout AS interfacetimeout,
    l.requestinterfacetimeout AS requestinterfacetimeout,
    e.clientname AS clientname,
    e.environment AS environment,
    l.ispracticetest AS ispracticetest,
    l.refreshvalue AS refreshvalue,
    l.tainterfacetimeout AS tainterfacetimeout,
    l.tacheckintime AS tacheckintime,
    l.datechanged AS datechanged,
    l.datepublished AS datepublished,
    l.sessionexpire AS sessionexpire,
    l.refreshvaluemultiplier AS refreshvaluemultiplier 
  FROM
    configs.client_timelimits l
  JOIN
    session._externs e
  WHERE
    e.clientname = l.clientname;
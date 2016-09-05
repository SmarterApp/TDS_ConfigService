/***********************************************************************************************************************
  File: V1472923813__session_create_view_externs.sql

  Desc: Create the externs view in the session database and load it with seed data.  The table creation
  and seed data are intended to support integration tests.  The schema and seed data are representative of what is
  deployed when a TDS system is deployed.

  The query to fetch client system flag information from the configs database relies on the session.externs view, which
  relies on this table being present (as well as sevral others).  This means that there are some instances where the
  Config service will query data from the session database.

  NOTE: Is this view required?  The only data collected from the session database is the session._externs.clientname and
  session._externs.environment values.  The configs.client_externs table has the same values with the same data:

    select c._key, c.clientname, c.environment from configs.client_externs c;
    # _key, clientname, environment
    ?, 'SBAC', 'Development'
    ?, 'SBAC_PT', 'Development'

  select clientName, environment from session._externs; -- this is the TABLE, not the VIEW.  The "foo", "bar" record was
  inserted by me at some point during troubleshooting the deployment process.

    # clientName, environment
    'foo', 'bar'
    'SBAC', 'Development'
    'SBAC_PT', 'Development'

  Auth:  Jeff Johnson <jeffjohnson9046>

***********************************************************************************************************************/
USE session;
CREATE OR REPLACE VIEW externs AS
  SELECT
    DISTINCT x.clientname AS clientname,
             e.testeedb AS testeedb,
             e.proctordb AS proctordb,
             'session' AS sessiondb,
             e.testeetype AS testeetype,
             e.proctortype AS proctortype,
             e.clientstylepath AS clientstylepath,
             x.environment AS environment,
             e.ispracticetest AS ispracticetest,
             (SELECT a.url FROM configs.geo_clientapplication a WHERE a.clientname = x.clientname AND a.environment = x.environment AND a.servicetype = 'CHECKIN' AND a.appname = 'STUDENT') AS testeecheckin,
             (SELECT a.url FROM configs.geo_clientapplication a WHERE a.clientname = x.clientname AND a.environment = x.environment AND a.servicetype = 'CHECKIN' AND a.appname = 'PROCTOR') AS proctorcheckin,
             e.timezoneoffset AS timezoneoffset,
             e.publishurl AS publishurl,
             e.initialreportingid AS initialreportingid,
             e.initialsessionid AS initialsessionid,
             e.testdb AS testdb,
             e.qabrokerguid AS qabrokerguid
  FROM
    session._externs x
    JOIN
    configs.client_externs e
      ON (e.clientname = x.clientname
      AND e.environment = x.environment);
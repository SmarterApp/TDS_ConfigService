# TDS_ConfigService
## Overview
The `TDS_ConfigService` (aka Config Support Service) consists of two modules:

* **client:** Contains the POJOs/classes needed for a consumer to interact with the Config Support Service
* **service:** REST endpoints that provide TDS configuration data

## Build
* To build the **client**:
  * `mvn clean install -f /path/to/client/pom.xml`
* To build the **service**:
  * `mvn clean install -f /path/to/service/pom.xml`
* To build the service and run integration tests:
  * `mvn clean install -Dintegration-tests.skip=false -f /path/to/service/pom.xml`

## Integration Test Notes
* Integration tests are not run during a typical Maven build
* To run integration tests during a build, refer to the **Build** section above

### Database Migrations
* The database migrations stored in `src/test/resources/db/migration` are only intended to support integration tests.  As such, they represent a small subset of the overall database schema for the `configs` and `session` databases.
* Database Migrations are only executed when the `failsafe` plugin is configured to run.  By default, `failsafe` is configured to skip running integration tests when Maven builds the project.
* [Flyway](https://flywaydb.org/) is used to execute the database migrations against an H2 in-memory database before the integration tests are executed. 

### Naming Database Migration Files
* Naming database migration files uses the following convention (note there are two underscores between the timestamp and the database name):

V[***timestamp***]_\_[***database name***]\_[***DDL operation***]\_[***object name***].sql
* Example file name: `V1472923547__configs_create_table_client_systemflags.sql`
* To generate the timestamp, use [http://www.unixtimestamp.com/](http://www.unixtimestamp.com/) and copy the UTC timestamp

### Database Migrations Troubleshooting

#### Seed Data
* Loading data from a MySQL dump file into an H2 database requires some special handling:
  * Remove all backtick (`) characters from the SQL
  * Exported `bit` columns must be updated:
      * Replace all `,'\0',` with `,0,`
      * Replace all `,'',` with `,1,`  
  * To handle MySQL `VARBINARY` values (e.g. the UUIDs used as primary keys):
      * In MySQL, use `HEX` to get the `VARBINARY` value in a readable format, e.g.: `SELECT HEX(_key), clientname FROM configs.client_externs;`
      * Copy the "expanded"/readable `VARBINARY` value
      * In the `INSERT... VALUES` statement that is inserting the `VARBINARY` value, use H2's `X(value)` function to properly convert the input, for example:

`INSERT INTO client_externs VALUES (X'1FEFBFBD54EFBFBDEFBFBD154BEFBFBD','MultiClient_RTS_2013','MultiClient_RTS_2013','itembank',1,1,'RTS','RTS',1,'session',1,1,'SBAC',1,'SBAC',NULL,'Development',0,0,NULL,100000,1,NULL,NULL);`

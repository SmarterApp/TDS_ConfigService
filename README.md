# TDS_ConfigService
## Overview
The `TDS_ConfigService` (aka Config Support Service) consists of two modules:

* **client:** Contains the POJOs/classes needed for a consumer to interact with the Config Support Service
* **service:** REST endpoints that provide TDS configuration data

## Build
To build the **client**:

* `mvn clean install -f /path/to/client/pom.xml`

To build the **service**:

* `mvn clean install -f /path/to/service/pom.xml`

To build the service and run integration tests:
  
* `mvn clean install -Dintegration-tests.skip=false -f /path/to/service/pom.xml`

To build the service and its associated Docker image:

* `mvn clean install docker:build -f /path/to/service/pom.xml`

## Run
### Run in IDE
To run the Config Support Service in the IDE, un-comment the properties defined in the `service/src/main/resources/application.properties` file and set them to appropriate values.

### Run .JAR
To run the compiled jar built by one of the build commands above, use the following:

```
java -Xms256m -Xmx512m \
    -jar /path/to/target/tds-config-service-0.0.1-SNAPSHOT.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \
    --spring.datasource.url="jdbc:mysql://[db server name]:[db port]/configs" \
    --spring.datasource.username="[MySQL user name]" \
    --spring.datasource.password="[MySQL user password]" \
    --spring.datasource.type=com.zaxxer.hikari.HikariDataSource
```

### Run Docker Container
The Config Support Service requires several environment variables to be set prior to starting the `.jar`.  The environment variables are managed by an [environment file](https://docs.docker.com/engine/reference/commandline/run/#/set-environment-variables-e-env-env-file), which Docker uses to set the appropriate environment variables.

To create the environment file:

* Navigate to where the `docker-compose.yml` file is located
* Create a new file named `config-service.env`
* Open `config-service.env` in an editor and set the following values:

```
CONFIGS_DB_HOST=[IP address or FQDN of the MySQL database server that hosts the TDS configs and session databases]
CONFIGS_DB_PORT=[The port on which the MySQL database server listens]
CONFIGS_DB_NAME=[The name of the TDS configs database (typically "configs")]
CONFIGS_DB_USER=[The MySQL user account with sufficient privileges to read from the configs and session databases]
CONFIGS_DB_PASSWORD=[The password for the MySQL user account]
```

* Example `config-service.env` file:

```
CONFIGS_DB_HOST=tds-mysql-instance.example.com
CONFIGS_DB_PORT=3306
CONFIGS_DB_NAME=configs
CONFIGS_DB_USER=tds_user
CONFIGS_DB_PASSWORD=protohorsecarbattery
```
**NOTE:**  Any file with a `.env` extension will _not_ be committed to source control; the `.gitignore` is set to exclude files with a `.env` extension.  Therefore, sensitive information stored in this file will not be committed.

After the `config-service.env` file is saved, run the Config Support Service Docker container with the following commands:
 
```
mvn clean install docker:build -f /path/to/service/pom.xml
docker-compose up -d -f /path/to/docker-compose.yml
```

#### Additional Details for Interacting With Docker
The `Dockerfile` included in this repository is intended for use with [Spotify's Docker Maven plugin](https://github.com/spotify/docker-maven-plugin).  As such, the `docker build` command will fail because it cannot find the compiled `.jar`.

The Docker container can be started via `docker-compose` or `docker run`:

* The command for starting the container via `docker-compose`:  `docker-compose up -d -f /path/to/docker-compose.yml`
  * **NOTE:** If `docker-compose` is run in the same directory where the `docker-compose.yml` file is located, `docker-compose up -d` is sufficient to start the container
* Alternately, `docker run` can be used to start up the container:  `docker run -d -p [open port on host]:8080 --env-file /path/to/config-service.env sbacoss/tds-config-service`
  * example:  `docker run -d -p 23571:8080 --env-file config-service.env sbacoss/tds-config-service`

To see the list of running Docker containers, use the following command:

* `docker ps -a`
* Output will appear as follows:
 
```
CONTAINER ID        IMAGE                        COMMAND                CREATED             STATUS              PORTS                     NAMES
4b267a450d3b        sbacoss/tds-config-service   "/docker-startup.sh"   2 hours ago         Up 2 hours          0.0.0.0:23571->8080/tcp   docker_config_1
```
To tail the log files for the process(es) running on the Docker container:

* `docker logs -f [container id]`
  * **NOTE:**  To view the logs without tailing them, omit the `-f` from the command above
* example:  `docker logs -f 4b267a450d3b`

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
  * Exported `BIT` columns must be updated:
      * Replace all `,'\0',` (non-printable `null` character) with `,0,`
      * Replace all `,'',` (non-printable character `u0001` [`SOH` or `START OF HEADER`]) with `,1,`  
      * ***NOTE:***  Take care when replacing the exported `BIT` columns.  It is possilbe the export also has empty `VARCHAR` fields, which might also be replaced by using the patterns above.
  * To handle MySQL `VARBINARY` values (e.g. the UUIDs used as primary keys):
      * In MySQL, use `HEX` to get the `VARBINARY` value in a readable format, e.g.: `SELECT HEX(_key), clientname FROM configs.client_externs;`
      * Copy the "expanded"/readable `VARBINARY` value
      * In the `INSERT... VALUES` statement that is inserting the `VARBINARY` value, use H2's `X(value)` function to properly convert the input, for example:

`INSERT INTO client_externs VALUES (X'1FEFBFBD54EFBFBDEFBFBD154BEFBFBD','MultiClient_RTS_2013','MultiClient_RTS_2013','itembank',1,1,'RTS','RTS',1,'session',1,1,'SBAC',1,'SBAC',NULL,'Development',0,0,NULL,100000,1,NULL,NULL);`

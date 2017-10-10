# TDS_ConfigService
## Overview
The `TDS_ConfigService` (aka Config Support Service) consists of two modules:

* **client:** Contains the POJOs/classes needed for a consumer to interact with the Config Support Service
* **service:** REST endpoints that provide TDS configuration data

## Prerequisites

* Java 7
* Java 8
* Maven 3

## Build
To build the **client**:

* `mvn clean install -f /path/to/client/pom.xml`

To build the **service**:

* `mvn clean install -f /path/to/service/pom.xml`

To build the service and run integration tests:
  
* `mvn clean install -Dintegration-tests.skip=false -f /path/to/service/pom.xml`

In addition to the tools the project also uses [Maven toolchains](https://maven.apache.org/guides/mini/guide-using-toolchains.html) since the client needs to be built at Java 1.7 to support legacy applications.  You will need to either append to your current toolchain file or create a new one in your local .m2 directory.

**Sample Mac OS Version**  

```<?xml version="1.0" encoding="UTF8"?>
<toolchains>
  <!-- JDK toolchains -->
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>1.7</version>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>1.8</version>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

### Docker Support
The Config Support Service provides a `Dockerfile` for building a Docker image and a `docker-compose.yml` for running a Docker container that hosts the service `.jar`.  For the following command to work, the Docker Engine must be installed on the target build machine.  Resources for downloading and installing the Docker Engine on various operating systems can be found [here](https://docs.docker.com/engine/installation/).  For details on what Docker is and how it works, refer to [this page](https://www.docker.com/what-docker).

To build the service and its associated Docker image:

* `mvn clean install docker:build -f /path/to/service/pom.xml`

More notes can be found [here](documentation/docker_faq.md)

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

## Spring Configuration
The application uses Spring and Spring boot. The configuration options for this application can be found [here](documentation/spring_configuration.md)

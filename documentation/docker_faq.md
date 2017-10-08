# Docker Configuration

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
* Alternately, `docker run` can be used to start up the container:  `docker run -d -p [open port on host]:8080 --env-file /path/to/config-service.env fwsbac/tds-config-service`
  * example:  `docker run -d -p 23571:8080 --env-file config-service.env fwsbac/tds-config-service`

To see the list of running Docker containers, use the following command:

* `docker ps -a`
* Output will appear as follows:
 
```
CONTAINER ID        IMAGE                        COMMAND                CREATED             STATUS              PORTS                     NAMES
4b267a450d3b        fwsbac/tds-config-service   "/docker-startup.sh"   2 hours ago         Up 2 hours          0.0.0.0:23571->8080/tcp   docker_config_1
```
To tail the log files for the process(es) running on the Docker container:

* `docker logs -f [container id]`
  * **NOTE:**  To view the logs without tailing them, omit the `-f` from the command above
* example:  `docker logs -f 4b267a450d3b`

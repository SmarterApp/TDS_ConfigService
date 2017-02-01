#!/bin/sh
#-----------------------------------------------------------------------------------------------------------------------
# File:  docker-startup.sh
#
# Desc:  Start the tds-config-service.jar with the appropriate properties.
#
#-----------------------------------------------------------------------------------------------------------------------

java \
    -Dspring.datasource.url="jdbc:mysql://${CONFIG_DB_HOST}:${CONFIG_DB_PORT}/${CONFIG_DB_NAME}" \
    -Dspring.datasource.username="${CONFIG_DB_USER}" \
    -Dspring.datasource.password="${CONFIG_DB_PASSWORD}" \
    -Dspring.datasource.type=com.zaxxer.hikari.HikariDataSource \
    -Dspring.datasource.driver-class-name=com.mysql.jdbc.Driver \
    -jar /tds-config-service.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \

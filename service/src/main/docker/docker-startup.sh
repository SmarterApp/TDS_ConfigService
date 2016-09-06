#!/bin/sh
# Determine the amount of memory available to the container, for use with setting the Xms and Xmx values when starting
# the jar (in this case setting both values to 80% of free memory).
freeMem=`awk '/MemFree/ { print int($2/1024) }' /proc/meminfo`
s=$(($freeMem/10*8))
x=$(($freeMem/10*8))

java -Xms${s}m -Xmx${x}m \
    -jar /tds-config-service.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \
    --spring.datasource.url="jdbc:mysql://${CONFIGS_DB_HOST}:${CONFIGS_DB_PORT}/${CONFIGS_DB_NAME}" \
    --spring.datasource.username="${CONFIGS_DB_USER}" \
    --spring.datasource.password="${CONFIGS_DB_PASSWORD}" \
    --spring.datasource.type=com.zaxxer.hikari.HikariDataSource
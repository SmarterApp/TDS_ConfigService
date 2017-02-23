#-----------------------------------------------------------------------------------------------------------------------
# File:  Dockerfile
#
# Desc:  Create a Docker container to host the Config Support Service.
#
#-----------------------------------------------------------------------------------------------------------------------
FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

COPY docker-startup.sh docker-startup.sh
COPY tds-config-service-*.*.*-SNAPSHOT.jar tds-config-service.jar

RUN sh -c 'touch /tds-config-service.jar /docker-startup.sh'
RUN apk --no-cache add curl

ENTRYPOINT ["/docker-startup.sh"]
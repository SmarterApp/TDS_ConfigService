FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

COPY docker-startup.sh docker-startup.sh
COPY tds-config-service-0.0.1-SNAPSHOT.jar tds-config-service.jar

RUN sh -c 'touch /tds-config-service.jar /docker-startup.sh'

ENTRYPOINT ["/docker-startup.sh"]
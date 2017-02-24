#!/bin/sh
#-----------------------------------------------------------------------------------------------------------------------
# File:  docker-startup.sh
#
# Desc:  Start the tds-config-service.jar with the appropriate properties.
#
#-----------------------------------------------------------------------------------------------------------------------

java $JAVA_OPTS -jar /tds-config-service.jar

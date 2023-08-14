#!/bin/bash
#########################
### Starts the server ###
#########################

#if [ -n "$WEBBEEP_HOME" ]; then
 # cd $WEBBEEP_HOME
# fi

cd /home/hkms-apps/NewsMonitor

# host port
# java -cp $CP org.hyperdata.beeps.server.BeepServer hyperdata.it 8888

java -jar target/NewsMonitor-1.20.23-SNAPSHOT.jar it.danja.newsmonitor.standalone.Main

# 67.207.131.83 ???

#!/bin/bash
#########################
### Starts the server ###
#########################

#if [ -n "$NEWSMONITOR_HOME" ]; then
 # cd $NEWSMONITOR
# fi

cd /home/hkms-apps/NewsMonitor

java -jar target/NewsMonitor-1.20.23-SNAPSHOT.jar it.danja.newsmonitor.standalone.Main


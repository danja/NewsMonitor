NewsMonitor
===========

A feed aggregator/reader with topic-focussed discovery.

NewsMonitor is primarily designed for use as an OSGi module within the Fusepool (/Stanbol) system. It is however possible to build and run NewsMonitor essentially standalone, with data storage handled by an external SPARQL 1.1 server/store. Instructions are provided below for use with Fuseki, although any SPARQL endpoint which supports query and update may be used. (It is assumed that there is no authentication required by the endpoint for POSTed updates).


## Integrated with Fusepool/Stanbol

### Installation

cd to NewsMonitor directory, then :

mvn clean install

or, skipping tests :

mvn clean install -Dmaven.test.skip=true

### Standalone, with external Fuseki SPARQL server

mvn clean install -P build-for-fuseki

### Running

java -jar NewsMonitor-1.0.0-SNAPSHOT.jar it.danja.newsmonitor.main.Main

# NewsMonitor

Revisiting 2021... not yet working again, see [devlog.md](devlog.md)

A feed aggregator/reader with topic-focussed discovery.

NewsMonitor may be run standalone with a SPARQL 1.1 store or as an OSGi module within the Fusepool (/Stanbol) system.

### Standalone, with external Fuseki SPARQL server

Assuming there's a Fuseki server running on http://localhost:3030 with a dataset called "feedreader".

#### To build :

First git clone this repository, then -

cd NewsMonitor

mvn clean install -P build-for-fuseki

### To run :

java -jar target/NewsMonitor-1.0.0-SNAPSHOT.jar it.danja.newsmonitor.standalone.Main

---

_The following might not currently work_

## Integrated with Fusepool/Stanbol

### Installation

cd to NewsMonitor directory, then :

mvn clean install

or, skipping tests :

mvn clean install -Dmaven.test.skip=true

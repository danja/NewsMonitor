# NewsMonitor

A feed aggregator with topic-focussed discovery.

NewsMonitor may be run standalone locally or on a remote server as long as there's a writeable [SPARQL](https://en.wikipedia.org/wiki/SPARQL) store somewhere accessible over HTTP. It's written in Java and was originally written as an OSGi module for the Fusepool (/Stanbol) system, though that aspect hasn't been mainained. 

### Status 2023-08-04**  

Revisiting. After adjusting config to point to [my online Fuseki store](https://fuseki.hyperdata.it/) and rebuilding, the aggregator appears to mostly work. Pretty remarkable given that it's almost a decade since I wrote it. Looks like it polls/trawls feeds as it should, but I'm not seeing the feed item content in the store yet. I suspect this is down to Fuseki having a different auth setup since I last tried it, so SPARQL INSERT isn't working. Fortunately the code seems pretty well structured (I got some EU funding for it, so had to attempt best practices), shouldn't be hard to fix. *Is funny, Java was my go-to language back then, haven't used it since. For Web stuff nowadays I'll use vanilla JS in the browser, nodejs for services, for general stuff Python, for embedded, C++.*

I have *absolutely no idea* what I had for client, can't see anything in the repo server- or client-side. But I've got stuff around [HKMS](https://hyperdata.it/hkms/) which I can easily adapt (the main idea behind HKMS is headless knowledge management, having task/domain-specific browser client apps that talk to common online SPARQL stores).

I pretty much abandoned NewsMonitor when I'd done enough for the contract, had other work to chase. The big thing I felt would benefit from back then was a little work on it's intelligence. If I remember correctly, when it discovers new feeds it does categorisation by string-matching on keywords. Something smarter, maybe [k-nearest neighbours](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) could be plugged in fairly easily.

But for now my aim is just to get it running again as a feed reader, mostly for my own benefit, but the news page it'll make might be of interest to anyone that like AI, Linked Data, modular synths and/or woodcarving.


### Running standalone (with external Fuseki SPARQL server)

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

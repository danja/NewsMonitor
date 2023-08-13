# NewsMonitor

A feed aggregator with topic-focussed discovery.

NewsMonitor may be run standalone locally or on a remote server as long as there's a writeable [SPARQL](https://en.wikipedia.org/wiki/SPARQL) store somewhere accessible over HTTP. It's written in Java and was originally intended as an OSGi module for the Fusepool (/Stanbol) system, though that aspect hasn't been maintained.

### Status 2023-08-13

Started revisiting this month.

It _almost_ worked right away. I had auth issues between it and the [Fuseki](https://jena.apache.org/documentation/fuseki2/) server. I ended up stripping back all the auth bits. At which point I realised that I'd wasted a lot of time because my test queries weren't the same shape as the data (I was putting each feed into it's own named graph but when testing I assumed everything was going in the default graph). Oops.

Since writing this thing I have been building the headless apps around [HKMS](https://hyperdata.it/hkms/) generally using what I call the **[SPARQL Diamonds](https://hyperdata.it/sparql-diamonds/)** pattern. In this pattern the browser client calls a SPARQL store and templates the results into HTML (and does similar with any input, form data or whatever : templates it into SPARQL queries with INSERT etc). As a lingua franca I've been using [Markdown](https://en.wikipedia.org/wiki/Markdown).

So with NewMonitor I want to shift to the same approach. Feed post content is saved in RDF literals as markdown. I've pretty much got this going server-side, I now need to play with the [client browser app](https://hyperdata.it/newsmonitor/river.html) to add the markdown rendering to make it all useful/pretty.

As well as making NewsMonitor consistent with the other HKMS apps, a bonus is that it should help sanitise/normalise the raw data. This, at some point, I want to use as training data in a small language model, like [Karpathy's llama2.c](https://github.com/karpathy/llama2.c).

_I pretty much abandoned NewsMonitor when I'd done enough for the contract, had other work to chase. The big thing I felt would benefit from back then was a little work on it's intelligence. If I remember correctly, when it discovers new feeds it does categorisation by string-matching on keywords. Something smarter, maybe [k-nearest neighbours](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) could be plugged in fairly easily._

_But for now my aim is just to get it running again as a feed aggregator service with simple browser rendering. This is mostly for my own benefit, though the news page it'll make might be of interest to anyone that like AI, Linked Data, modular synths and/or woodcarving._

#### ToDo

- reintroduce auth on updates/inserts
- add markdown rendering to browser clients
- add a smarter classifier
- make everything useful and pretty

### Running standalone (with external Fuseki SPARQL server)

Assuming there's a Fuseki server running on http://localhost:3030 with a dataset called "feedreader". 2023 : _host:port has to go in standalone-config.properties_

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

**2023-08**

## systemctl

added newsmonitor.service

logs -

journalctl -u newsmonitor.service

journalctl -u newsmonitor.service |tail -30

## feedlists

danny@danny-desktop:~/HKMS/NewsMonitor/2023/js$ nodejs feed-grabber.js https://github.com/ChristosChristofidis/awesome-deep-learning

## fuseki backups

https://fuseki.hyperdata.it/$/backups-list

/etc/fuseki/backups/newsmonitor_2023-08-12_21-26-44.nq.gz

https://jena.apache.org/documentation/fuseki2/fuseki-layout.html

## Fuseki clean

Fuseki UI, choose /newsmonitor/

DROP ALL

## markdown

HTML -> markdown is in
FeedHandlerBase.resolveContent()

had to remove a load of content-related tests from parsers/interpreters

https://github.com/vsch/flexmark-java/blob/master/flexmark-html2md-converter/src/main/java/com/vladsch/flexmark/html2md/converter/FlexmarkHtmlConverter.java

---

danny@danny-desktop:~/HKMS/NewsMonitor$ mvn clean install -P build-for-fuseki

mvn test

runs them all

mvn test -Dtest=it.danja.newsmonitor.tests.http.TestSparqlUpdate

PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?class ?label ?description
WHERE {
?s rdf:type ?class .
OPTIONAL { ?class rdfs:label ?label}
OPTIONAL { ?class rdfs:comment ?description}
}
LIMIT 25

PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
INSERT DATA
{
<http://example/book1> rdf:type <http://example/Y> .
}

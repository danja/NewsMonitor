download [Fuseki2](https://jena.apache.org/documentation/fuseki2/)

unzip

-------------

mvn build didn't work at first had to delete line
```<packaging>bundle</packaging>```
from pom.xml

mvn clean install -P build-for-fuseki -Dmaven.test.skip=true

(mvn clean install -Dmaven.test.skip=true)

[INFO] Installing /home/danny/dev/NewsMonitor/target/NewsMonitor-1.0.0-SNAPSHOT.jar to /home/danny/.m2/repository/it/danja/NewsMonitor/1.0.0-SNAPSHOT/NewsMonitor-1.0.0-SNAPSHOT.jar

running from NetBeans
no Fuseki running :
819 [main] ERROR it.danja.newsmonitor.io.SparqlConnector - Connect to localhost:3030 [localhost/127.0.0.1] failed: Connection refused
Exception in thread "main" java.lang.NullPointerException

SPARQL config is in :
./src/main/resources/standalone-config.properties

http://localhost:3030/

create new dataset : feedreader

topics loaded in it.danja.newsmonitor.discovery

topic URI is hardcoded in FeedListLoader

PREFIX rss: <http://purl.org/rss/1.0/>
PREFIX dcterms:<http://purl.org/dc/terms/>
PREFIX foaf:<http://xmlns.com/foaf/0.1/>
PREFIX schema:<http://schema.org/>
PREFIX nm: <http://purl.org/stuff/newsmonitor/>

SELECT DISTINCT ?label ?href ?rel ?contentType ?format ?explored ?relevance ?origin WHERE {

	GRAPH ?g {
	  ?link a nm:Link ;
	  nm:explored "false" .
	   OPTIONAL { ?link nm:origin ?origin . }
		OPTIONAL { ?link nm:label ?label . }
	    OPTIONAL { ?link nm:href ?href . }
	    OPTIONAL { ?link nm:rel ?rel . }
	    OPTIONAL { ?link nm:responseCode ?responseCode . }
	    OPTIONAL { ?link nm:contentType ?contentType . }
	    OPTIONAL { ?link nm:format ?format . }
	    OPTIONAL { ?link nm:relevance ?relevance . }
    }
} 

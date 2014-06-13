PREFIX rss: <http://purl.org/rss/1.0/>
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX schema: <http://schema.org/>
PREFIX nm: <http://purl.org/stuff/newsmonitor/>

WITH <http://purl.org/stuff/newsmonitor/status> DELETE { 
?s ?p ?o 
} WHERE { 
GRAPH <http://purl.org/stuff/newsmonitor/status> {
     ?s ?p ?o 
     }
  } ;
INSERT DATA { GRAPH <http://purl.org/stuff/newsmonitor/status> { 
	<http://purl.org/stuff/newsmonitor/status> 
		nm:pollerRunning "${pollerRunning?c}";
		nm:discoveryRunning "${pollerRunning?c}"; .
 }}
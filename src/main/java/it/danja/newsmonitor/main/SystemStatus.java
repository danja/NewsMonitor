/**
 * NewsMonitor
 *
 * SystemStatus.java
 * @author danja
 * @date Jun 13, 2014
 *
 */
package it.danja.newsmonitor.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.FeedListLoader.LineHandler;
import it.danja.newsmonitor.sparql.SparqlResults;
import it.danja.newsmonitor.sparql.SparqlResultsParser;
import it.danja.newsmonitor.templating.Templater;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SystemStatus {
	
	private static Logger log = LoggerFactory.getLogger(SystemStatus.class);

	private boolean pollerRunning = true;
	private boolean discoveryRunning = true;
	
	private static final String sparqlGetStatus;
	//private static final String updateStatusTemplate;
	
	static {
		sparqlGetStatus = TextFileReader.read(Config.SPARQL_GET_STATUS);
	//	updateStatusTemplate = TextFileReader.read(Config.UPDATE_STATUS_TEMPLATE);
	}
	
	/**
	 * 
	 */
	public SystemStatus() {
		// TODO Auto-generated constructor stub
	}
	
public void setPollerRunning(boolean pollerRunning) {
	this.pollerRunning = pollerRunning;
	pushStatusToStore();
}

public boolean getPollerRunning() {
	pullStatusFromStore();
	return pollerRunning;
}

public void setDiscoveryRunning(boolean discoveryRunning) {
	this.discoveryRunning = discoveryRunning;
	pushStatusToStore();
}

public boolean getDiscoveryRunning() {
	pullStatusFromStore();
	return discoveryRunning;
}

public void initializeFeedListFromFile(String filename) {
	log.info("Loading feed list from file "+filename+" into store...");
	FeedListLoader loader = new FeedListLoader();
	LineHandler handler = loader.new LineHandler();

	String turtleBody = loader.readFile(filename, handler);
	String sparql = FeedListLoader.insertValue(
			FeedListLoader.SPARQL_TEMPLATE, "channels", turtleBody);
	// log.info("Query = \n" + sparql);
	int responseCode = SparqlConnector.update(
			Config.UPDATE_ENDPOINT, sparql).getStatusCode();
	log.info("SPARQL response : "+responseCode);
}

private void pushStatusToStore(){
		  Map<String, Object> map = new HashMap<String, Object>();
		  map.put("pollerRunning", pollerRunning);
		  map.put("discoveryRunning", discoveryRunning);

		  String sparql = Templater.apply("update-status", map);
		  SparqlConnector.update(Config.UPDATE_ENDPOINT, sparql);
	  }

private void pullStatusFromStore(){
	// log.info("A");
	String results = SparqlConnector.query(Config.QUERY_ENDPOINT, sparqlGetStatus);
	// log.info("B");
	SparqlResultsParser parser = new SparqlResultsParser();
	// log.info("C");
	SparqlResults sparqlResults = parser.parse(results);
	// log.info("D");
//	log.info("Size in main = "+sparqlResults.getResults().size());
	log.info(sparqlResults.getResults().toString());
}
}

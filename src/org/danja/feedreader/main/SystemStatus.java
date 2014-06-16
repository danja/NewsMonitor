/**
 * feedreader-prototype
 *
 * SystemStatus.java
 * @author danja
 * @date Jun 13, 2014
 *
 */
package org.danja.feedreader.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.danja.feedreader.io.SparqlConnector;
import org.danja.feedreader.io.TextFileReader;
import org.danja.feedreader.main.FeedListLoader.LineHandler;
import org.danja.feedreader.sparql.SparqlResults;
import org.danja.feedreader.sparql.SparqlResultsParser;
import org.danja.feedreader.templating.Templater;

/**
 *
 */
public class SystemStatus {

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
	System.out.println("Loading feed list from file "+filename+" into store...");
	FeedListLoader loader = new FeedListLoader();
	LineHandler handler = loader.new LineHandler();

	String turtleBody = loader.readFile(filename, handler);
	String sparql = FeedListLoader.insertValue(
			FeedListLoader.SPARQL_TEMPLATE, "channels", turtleBody);
	// System.out.println("Query = \n" + sparql);
	int responseCode = SparqlConnector.update(
			Config.UPDATE_ENDPOINT, sparql).getStatusCode();
	System.out.println("SPARQL response : "+responseCode);
}

private void pushStatusToStore(){
		  Map<String, Object> map = new HashMap<String, Object>();
		  map.put("pollerRunning", pollerRunning);
		  map.put("discoveryRunning", discoveryRunning);

		  String sparql = Templater.apply("update-status", map);
		  SparqlConnector.update(Config.UPDATE_ENDPOINT, sparql);
	  }

private void pullStatusFromStore(){
	System.out.println("A");
	String results = SparqlConnector.query(Config.QUERY_ENDPOINT, sparqlGetStatus);
	System.out.println("B");
	SparqlResultsParser parser = new SparqlResultsParser();
	System.out.println("C");
	SparqlResults sparqlResults = parser.parse(results);
	System.out.println("D");
//	System.out.println("Size in main = "+sparqlResults.getResults().size());
	System.out.println(sparqlResults.getResults());
}
}

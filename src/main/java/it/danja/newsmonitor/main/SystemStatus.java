/**
 * NewsMonitor
 *
 * SystemStatus.java
 * @author danja
 * @date Jun 13, 2014
 *
 */
package it.danja.newsmonitor.main;

import org.osgi.framework.BundleContext;
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

            private SparqlConnector sparqlConnector = new SparqlConnector();
            
	private boolean pollerRunning = true;
	private boolean discoveryRunning = true;

	private BundleContext bundleContext;

	private final String sparqlGetStatus;

	// private static final String updateStatusTemplate;

	public SystemStatus(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		// log.debug(bundleContext.toString());
		if (Config.BUILD_TYPE == Config.STANDALONE_BUILD) {
			sparqlGetStatus = TextFileReader
					.readFromFilesystem(Config.SPARQL_GET_STATUS_FILE);
		} else {
			sparqlGetStatus = TextFileReader.readFromBundle(
					bundleContext.getBundle(), Config.SPARQL_GET_STATUS_IN_BUNDLE);
		}
		// updateStatusTemplate =
		// TextFileReader.read(Config.UPDATE_STATUS_TEMPLATE);
	}

	// public void setBundleContext(BundleContext bundleContext) {
	// this.bundleContext = bundleContext;
	// }

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

	public void initializeFeedListFromFile() {
		// log.info("Loading feed list from file " + filename + " into store...");
		FeedListLoader loader = new FeedListLoader(bundleContext);
		

		String turtleBody = loader.readFile();
		String sparql = FeedListLoader.insertValue(
				FeedListLoader.SPARQL_TEMPLATE, "channels", turtleBody);
		// log.info("Query = \n" + sparql);
		int responseCode = sparqlConnector.update(Config.UPDATE_ENDPOINT,
				sparql).getStatusCode();
		log.info("SPARQL response : " + responseCode);
	}

	private void pushStatusToStore() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pollerRunning", pollerRunning);
		map.put("discoveryRunning", discoveryRunning);

		String sparql = Templater.apply("update-status", map);
		sparqlConnector.update(Config.UPDATE_ENDPOINT, sparql);
	}

	private void pullStatusFromStore() {
		// log.info("A");
		String results = sparqlConnector.query(Config.QUERY_ENDPOINT,
				sparqlGetStatus);
		// log.info("B");
		SparqlResultsParser parser = new SparqlResultsParser();
		// log.info("C");
		SparqlResults sparqlResults = parser.parse(results);
		// log.info("D");
		// log.info("Size in main = "+sparqlResults.getResults().size());
		log.info(sparqlResults.getResults().toString());
	}
}

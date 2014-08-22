/**
 * NewsMonitor
 *
 * FeedUrls.java
 * @author danja
 * @date Apr 23, 2014
 *
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.sparql.SparqlResultsParser;
import it.danja.newsmonitor.sparql.SparqlResults.Result;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

/**
 *
 */
public class FeedUrls  {

	private List<String> feeds = new ArrayList<String>();

	private BundleContext bundleContext;
	
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void load() {
		String sparql = null;
		if(Config.BUILD_TYPE == Config.STANDALONE_BUILD) {
			sparql = TextFileReader.readFromFilesystem(Config.SPARQL_FEEDLIST_FILENAME);
			} else {
				sparql = TextFileReader.readFromBundle(bundleContext.getBundle(), Config.SPARQL_FEEDLIST_FILENAME);
			}
		//	updateStatusTemplate = TextFileReader.read(Config.UPDATE_STATUS_TEMPLATE);
		
		
		
		
		String xmlResults = SparqlConnector.query(Config.QUERY_ENDPOINT, sparql);

		/*
		 * is no doubt less efficient than using 
		 * source = new InputSource(url.openStream())
		 * but it does get the media types right
		 * and doesn't have to run very often
		 */
		SparqlResultsParser parser = new SparqlResultsParser();
		List<Result> results = parser.parse(xmlResults).getResults();
		for (int i = 0; i < results.size(); i++) {
			// log.info(results.get(i));
			String feed = results.get(i).iterator().next().getValue();
			feeds.add(feed);
		}
	}
	
	public List<String> getFeeds(){
		return feeds;
	}

}

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

/**
 *
 */
public class FeedUrls  {

	private List<String> feeds = new ArrayList<String>();

	public void load() {
		String sparql = TextFileReader.read(Config.SPARQL_FEEDLIST_FILENAME);
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
			// System.out.println(results.get(i));
			String feed = results.get(i).iterator().next().getValue();
			feeds.add(feed);
		}
	}
	
	public List<String> getFeeds(){
		return feeds;
	}

}

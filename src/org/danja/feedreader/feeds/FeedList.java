/**
 * feedreader-prototype
 *
 * FeedList.java
 * @author danja
 * @date Apr 23, 2014
 *
 */
package org.danja.feedreader.feeds;

import java.util.ArrayList;
import java.util.List;

import org.danja.feedreader.io.SparqlConnector;
import org.danja.feedreader.io.TextFileReader;
import org.danja.feedreader.parsers.SparqlResults.Result;
import org.danja.feedreader.parsers.SparqlResultsParser;

/**
 *
 */
public class FeedList  {

	// TODO move to config
	private static String SPARQL_FILENAME = "sparql/get-feedlist.sparql";
	private static String queryEndpoint = "http://localhost:3030/feedreader/query";
	private List<String> feeds = new ArrayList();

	public void load() {
		String sparql = TextFileReader.read(SPARQL_FILENAME);
		String xmlResults = SparqlConnector.query(queryEndpoint, sparql);

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
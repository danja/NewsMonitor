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
import it.danja.newsmonitor.standalone.FsTextFileReader;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

/**
 *
 */
public class FeedUrls  {

	private List<String> feeds = new ArrayList<String>();

	private TextFileReader textFileReader = null;
        
	public void setTextFileReader(TextFileReader textFileReader) {
		this.textFileReader = textFileReader;
	}

	public void load(String location) {
		String sparql = textFileReader.read(location); // SPARQL_FEEDLIST_LOCATION
		
		
		   SparqlConnector sparqlConnector = new SparqlConnector();
		
		String xmlResults = sparqlConnector.query(Config.QUERY_ENDPOINT, sparql);

		/*
		 * is no doubt less efficient than using 
		 * source = new InputSource(url.openStream())
		 * but it does get the media types right
		 * and doesn't have to run very often
		 */
		SparqlResultsParser parser = new SparqlResultsParser();
               // parser.setSparql(sparql); // for debugging
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

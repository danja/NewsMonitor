/**
 * NewsMonitor - FeedUrls
 *
 * This class is responsible for managing the URLs of feeds. It loads feed URLs
 * from a specified location, performs SPARQL queries, and parses the results
 * to store the feed URLs in a list.
 *
 * @author danja
 * @version 1.20.23
 * dc:date 2023-08-14
 * @since Apr 23, 2014
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.sparql.SparqlResults.Result;
import it.danja.newsmonitor.sparql.SparqlResultsParser;
import it.danja.newsmonitor.standalone.FsTextFileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.osgi.framework.BundleContext;

public class FeedUrls {

  private List<String> feeds = new ArrayList<String>();
  private TextFileReader textFileReader = null;
  private Properties config = null;

  /**
   * Constructs a FeedUrls object with the given configuration and TextFileReader.
   *
   * @param config the properties configuration
   * @param textFileReader the reader to read text files
   */
  public FeedUrls(Properties config, TextFileReader textFileReader) {
    this.textFileReader = textFileReader;
    this.config = config;
  }

  /**
   * Loads feed URLs from the specified location, performs SPARQL queries, and
   * stores the URLs in the feeds list.
   *
   * @param location the location of the SPARQL feed list
   */
  public synchronized void load(String location) {
    String sparql = textFileReader.read(location); // SPARQL_FEEDLIST_LOCATION

    // System.out.println("SPARQL_FEEDLIST_LOCATION = " + sparql);
    SparqlConnector sparqlConnector = new SparqlConnector(config);

    String xmlResults = sparqlConnector.query(
      config.getProperty("QUERY_ENDPOINT"),
      sparql
    );

    SparqlResultsParser parser = new SparqlResultsParser();
    List<Result> results = parser.parse(xmlResults).getResults();
    for (int i = 0; i < results.size(); i++) {
      String feed = results.get(i).iterator().next().getValue();
      // System.out.println("FEED = " + feed);
      feeds.add(feed);
    }
  }

  /**
   * Retrieves the list of feed URLs.
   *
   * @return a list of feed URLs
   */
  public List<String> getFeeds() {
    return feeds;
  }
}

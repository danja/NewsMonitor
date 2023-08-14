/**
 * NewsMonitor class is responsible for managing the polling and exploration of news feeds.
 * It initializes, starts, and stops the poller and link explorer for the feeds.
 *
 * @author danja
 * @version 1.20.23
 * dc:date 2023-08-14
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.discovery.LinkExplorer;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.sparql.SparqlTemplater;
import it.danja.newsmonitor.templating.Templater;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;

public class NewsMonitor {

  private static Logger log = LoggerFactory.getLogger(NewsMonitor.class);
  private LinkExplorer linkExplorer;
  private Poller poller;

  private Properties config = null;
  private Templater templater = null;
  private TextFileReader textFileReader = null;

  /**
   * Constructor to create a NewsMonitor object.
   *
   * @param config          Properties containing the configuration parameters.
   * @param textFileReader  TextFileReader to read text files.
   * @param templater       Templater used for templating functionality.
   */
  public NewsMonitor(
    Properties config,
    TextFileReader textFileReader,
    Templater templater
  ) {
    this.templater = templater;
    this.config = config;
    this.textFileReader = textFileReader;
  }

  /**
   * Starts the monitoring of news feeds, initializes the feed list,
   * loads seed list, and starts Poller and LinkExplorer.
   *
   * @see Poller
   * @see LinkExplorer
   */
  public void start() {
    SystemStatus status = new SystemStatus(config, textFileReader, templater);
    status.initializeFeedListFromFile();
    SparqlTemplater sparqlTemplater = new SparqlTemplater(
      config,
      textFileReader,
      templater
    );
    poller = new Poller(config, sparqlTemplater);
    log.info("Loading feed list from store...");
    poller.setFeedUrls(getFeeds());
    log.info("==== Initialising Feeds ====");
    poller.initFeeds();
    linkExplorer =
      new LinkExplorer(config, poller.getFeedList(), textFileReader, templater);
    log.info("==== Starting Poller & LinkExplorer ====");
    poller.start();
    linkExplorer.start();

    if (Integer.parseInt(config.getProperty("TEST_RUN")) > 0) {
      try {
        Thread.sleep(Integer.parseInt(config.getProperty("TEST_RUN"))); // wait a bit
      } catch (InterruptedException e) {
        log.error(e.getMessage());
      }
      stop();
      poller.displayFeeds();
      log.info("\n==== Stopped Poller ====");
      while (!poller.isStopped() || !linkExplorer.isStopped()) {
        try {
          Thread.sleep(1000); // wait a bit
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }
      }
    }
  }

  /**
   * Stops the Poller and LinkExplorer.
   */
  public void stop() {
    poller.stop();
    linkExplorer.stop();
  }

  /**
   * Fetches the feed URLs from the specified SPARQL feed list location.
   *
   * @return A list of feed URLs.
   */
  private List<String> getFeeds() {
    FeedUrls feedUrlList = new FeedUrls(config, textFileReader);
    String feedlistLocation = config.getProperty("SPARQL_FEEDLIST_LOCATION");
    log.info("feedlistLocation = " + feedlistLocation);
    feedUrlList.load(feedlistLocation);
    return feedUrlList.getFeeds();
  }
}

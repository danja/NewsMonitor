/**
 * NewsMonitor
 *
 * Poller.java
 *
 * This class is responsible for managing and refreshing a list of feed URLs.
 * It provides functionality to start, stop, and run the polling process,
 * and handle feeds according to their content and relevance.
 *
 * @author danja
 * @version 1.20.23
 * @date 2023-08-14
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedList;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.FeedImpl;
import it.danja.newsmonitor.model.impl.FeedListImpl;
import it.danja.newsmonitor.sparql.SparqlTemplater;
import it.danja.newsmonitor.utils.ContentType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Poller implements Runnable {

  private static Logger log = LoggerFactory.getLogger(Poller.class);
  private List<String> feedUrls = null;
  private FeedList feedList = null;

  /**
   * Returns the feed list managed by the Poller.
   *
   * @return the feedList
   */
  public FeedList getFeedList() {
    return feedList;
  }

  private boolean running = false;
  private Thread thread;
  private int loopCount = 0;
  private boolean stopped = false;
  private SparqlTemplater sparqlTemplater = null;
  private Properties config = null;

  /**
   * Constructor to initialize the Poller with configuration properties and SPARQL templater.
   *
   * @param config           Configuration properties for the Poller
   * @param sparqlTemplater  SPARQL templater for handling feed data
   */
  public Poller(Properties config, SparqlTemplater sparqlTemplater) {
    this.sparqlTemplater = sparqlTemplater;
    this.config = config;
    feedList = new FeedListImpl(config);
  }

  /**
   * Initializes feeds by creating Feed objects for each URL and adding them to the FeedList.
   *
   * @return the initialized FeedList
   */
  public FeedList initFeeds() {
    for (int i = 0; i < feedUrls.size(); i++) {
      Feed feed = new FeedImpl(config);
      feed.setUrl(feedUrls.get(i));
      boolean redirect = feed.init();
      if (redirect) {
        String location = feed.getLocation();
        feed = new FeedImpl(config);
        feed.setUrl(location);
        feed.init();
      }
      feedList.addFeed(feed);
    }
    log.info("==== FeedList ====");
    log.info(feedList.toString());
    return feedList;
  }

  /**
   * Starts the polling process by creating a new thread.
   */
  public void start() {
    stopped = false;
    thread = new Thread(this);
    thread.start();
  }

  /**
   * Stops the polling process.
   */
  public void stop() {
    running = false;
  }

  /**
   * Main run method for the polling process. Continuously refreshes the feeds until stopped.
   */
  @Override
  public void run() {
    running = true;
    while (running) {
      if (feedList.size() == 0) {
        log.warn("No valid feeds, stopping poller...");
        running = false;
        break;
      }
      log.info("\n*** Starting loop #" + (++loopCount) + " ***");
      log.info("Refreshing " + feedList.size() + " feeds...");
      refreshFeeds();
    }
    log.info("Poller stopped.");
    stopped = true;
  }

  /**
   * Refreshes the feeds, handling expirations, relevancy, and other conditions.
   */
  public synchronized void refreshFeeds() {
    Set<Feed> expiring = new HashSet<Feed>();
    Iterator<Feed> iterator = feedList.getList().iterator();
    // feedQueue.iterator();
    Feed feed = null;

    while (iterator.hasNext()) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
      }
      feed = iterator.next();
      // log.info("feed.getLives() = "+feed.getLives());
      // log.info("feed.isDead() = "+feed.isDead());
      // if(!feed.isDead() && feed.getLives() < Config.MAX_LIVES) {
      // log.info("Less than max lives, re-initializing...");
      // feed.init();
      // // feed.setFirstCall(true);
      // }
      log.info("\nRefreshing : " + feed.getUrl());
      // feed.setFirstCall(firstCall);
      feed.refresh();

      // log.info("\nin poller feed.getLinks() : " + feed.getLinks());

      Set<Link> links = feed.getLinks();

      for (Link link : links) {
        feedList.addFeed(link.getHref());
      }

      if (feed.getFormatHint() == ContentType.HTML) { // shouldn't be
        // needed
        // feed.setDead(true);
        log.info("Is HTML...");
      }
      if (feed.getLives() < 1) {
        log.info("Lives gone...");
        feed.setDead(true);
      }
      if (
        feed.getRelevance() <
        Float.parseFloat(config.getProperty("UNSUBSCRIBE_RELEVANCE_THRESHOLD"))
      ) {
        log.info("Now below relevance threshold...");
        feed.setDead(true);
      }
      if (feed.isDead()) {
        log.info("Flagging as dead, skipping.");

        // TODO is duplicated below
        log.info("Unsubscribing from " + feed.getUrl());
        feedList.remove(feed);
        continue;
      }
      if (feed.shouldExpire()) {
        expiring.add(feed);
      }

      try {
        Thread.sleep(
          Integer.parseInt(config.getProperty("PER_FEED_SLEEP_PERIOD"))
        );
      } catch (InterruptedException e) {
        log.error(e.getMessage());
      }
      if (feed.isNew()) {
        pushFeed(feed);
      } else {
        log.info("No changes to : " + feed.getUrl());
      }
    }

    if ("true".equals(config.getProperty("POLLER_NO_LOOP"))) {
      System.exit(1);
    }

    iterator = expiring.iterator();
    while (iterator.hasNext()) {
      feed = iterator.next();
      log.info("Unsubscribing from " + feed.getUrl());
      feedList.remove(feed);
    }

    try {
      Thread.sleep(Integer.parseInt(config.getProperty("REFRESH_PERIOD")));
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Pushes a feed to the server using SPARQL.
   *
   * @param feed the Feed object to be pushed
   */
  private void pushFeed(Feed feed) {
    System.out.println(
      "<\n\n--------------------------------POST-------------------------->"
    );
    System.out.println("Uploading SPARQL for : " + feed.getUrl());
    log.info("Uploading SPARQL for : " + feed.getUrl());
    HttpMessage message = sparqlTemplater.uploadFeed(feed);
    feed.clean();
    log.info(
      "SPARQL response = " +
      message.getStatusCode() +
      " " +
      message.getStatusMessage()
    );
    if (message.getStatusCode() >= 400) {
      log.info("\n" + message + "\n");
    }
  }

  /**
   * Displays the list of feeds to the logger.
   */
  public void displayFeeds() {
    Iterator<Feed> feedIterator = feedList.getList().iterator();
    while (feedIterator.hasNext()) {
      log.info(feedIterator.next().toString());
    }
    log.info("---------------");
    // for (int i = 0; i < entries.size(); i++) {
    // log.info(entries.getEntry(i));
    // }
  }

  /**
   * Sets the feed URLs to be managed by the Poller.
   *
   * @param feedUrls the list of feed URLs
   */
  public void setFeedUrls(List<String> feedUrls) {
    this.feedUrls = feedUrls;

    List<String> tweakedUrls = new ArrayList<String>(); // little temp workaround for some old feeds
    for (int i = 0; i < feedUrls.size(); i++) {
      String url = feedUrls.get(i);
      if (url.startsWith("http:")) {
        String tweaked = "https:" + url.substring(5);
        log.info("tweaked = " + tweaked);
        tweakedUrls.add(tweaked);
      }
    }
    this.feedUrls.addAll(tweakedUrls);
  }

  /**
   * Checks if the Poller has stopped.
   *
   * @return true if the Poller has stopped, false otherwise
   */
  public boolean isStopped() {
    return stopped;
  }
}

/**
 * NewsMonitor
 *
 * LinkExplorer class is responsible for exploring links pulled from a SPARQL store.
 * It checks each link for relevance and, where appropriate, applies feed autodiscovery.
 *
 * @author danja
 * @version 1.20.23
 * @since 2023-08-14
 */
package it.danja.newsmonitor.discovery;

import it.danja.newsmonitor.io.HttpConnector;
import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedList;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.LinkImpl;
import it.danja.newsmonitor.sparql.SparqlResults.Binding;
import it.danja.newsmonitor.sparql.SparqlResults.Result;
import it.danja.newsmonitor.sparql.SparqlResultsParser;
import it.danja.newsmonitor.templating.Templater;
import it.danja.newsmonitor.utils.ContentProcessor;
import it.danja.newsmonitor.utils.ContentType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkExplorer implements Runnable {

  private static Logger log = LoggerFactory.getLogger(LinkExplorer.class);

  private FeedList feedList = null;
  private boolean running = false;
  private Thread thread = null;
  private boolean stopped = false;
  private Link link;
  private HttpConnector httpConnector = null;
  private SparqlConnector sparqlConnector = null;
  private String url = null;
  private TextFileReader textFileReader = null;
  private Templater templater = null;
  private Properties config;

  /**
   * Constructor for LinkExplorer class.
   *
   * @param config Configuration properties.
   * @param feedList List of feeds to be explored.
   * @param textFileReader Text file reader for SPARQL queries.
   * @param templater Templater for SPARQL queries.
   */
  public LinkExplorer(
    Properties config,
    FeedList feedList,
    TextFileReader textFileReader,
    Templater templater
  ) {
    this.config = config;
    this.feedList = feedList;
    this.templater = templater;
    this.textFileReader = textFileReader;
    sparqlConnector = new SparqlConnector(config);
    httpConnector = new HttpConnector(config);
  }

  /**
   * Starts the link explorer.
   */
  public void start() {
    log.info("LINKEXPLORER START");
    running = true;
    stopped = false;
    thread = new Thread(this);
    thread.start();
  }

  /**
   * Stops the link explorer.
   */
  public void stop() {
    running = false;
  }

  /**
   * Core method for exploring links.
   */
  public void run() {
    while (running) {
      Set<Link> links = getLinksFromStore();
      Iterator<Link> linkIterator = links.iterator();
      while (linkIterator.hasNext()) {
        Link link = linkIterator.next();
        if (feedList.containsFeed(link.getHref())) {
          link.setExplored(true);
          continue;
        }
        if (link.isExplored()) {
          continue;
        }
        log.info("LINK : " + link);
        explore(link);
        link.setExplored(true);
        updateStore(link);
        try {
          Thread.sleep(
            Integer.parseInt(config.getProperty("LINK_EXPLORER_SLEEP_PERIOD"))
          );
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }
      }
    }
    stopped = true;
  }

  /**
   * Explores the provided link for relevant content.
   *
   * @param link Link to be explored.
   */
  private void explore(Link link) {
    if (link.isExplored()) {
      return;
    }
    if (link.getResponseCode() >= 400) {
      return;
    }
    if (
      !link.getHref().startsWith("http://") &&
      !link.getHref().startsWith("https://")
    ) {
      return;
    }
    log.info("*** Exploring " + link.getHref() + "...");
    this.link = link;
    this.url = link.getHref();
    httpConnector.setUrl(url);
    String data = httpConnector.downloadAsString(url);
    int responseCode = httpConnector.getResponseCode();
    link.setResponseCode(responseCode);
    if (data != null && responseCode == 200) {
      String contentType = httpConnector.getContentType();
      link.setContentType(contentType);
      char format = ContentType.identifyFormat(contentType, data);
      link.setFormat(ContentType.formatName(format));
      if (format == ContentType.UNKNOWN) {
        link.setRelevance(0F);
        return;
      }
      RelevanceCalculator relevanceCalculator = new RelevanceCalculator();
      float relevance = relevanceCalculator.calculateRelevance(
        Config.TOPIC,
        data
      );
      link.setRelevance(relevance);
      if (
        relevance >
        Float.parseFloat(config.getProperty("SUBSCRIBE_RELEVANCE_THRESHOLD"))
      ) {
        trySubscribe(link);
      }
    } else {
      link.setContentType(null);
      link.setFormat(ContentType.formatName(ContentType.UNKNOWN));
      link.setRelevance(0F);
    }
    link.setExplored(true);
  }

  /**
   * Tries to subscribe to the link if it's relevant.
   *
   * @param pageLink Link to be checked for subscription.
   */
  private void trySubscribe(Link pageLink) {
    log.info("*** Looking for a feed linked from : " + pageLink.getHref());
    HttpConnector connector = new HttpConnector(config);
    String content = connector.downloadAsString(pageLink.getHref());
    Set<Link> links = ContentProcessor.extractHeadLinks(
      pageLink.getOrigin(),
      content
    );
    Iterator<Link> iterator = links.iterator();
    while (iterator.hasNext()) {
      Link link = iterator.next();
      if (link.getRel() != null && link.getRel().equals("alternate")) {
        Feed newFeed = feedList.createFeed(link.getHref());
        newFeed.init();
        newFeed.setRelevance(link.getRelevance());
        feedList.addFeed(newFeed);
      }
    }
  }

  /**
   * Checks if the link explorer has stopped.
   *
   * @return true if stopped, false otherwise.
   */
  public boolean isStopped() {
    return stopped;
  }

  /**
   * Updates the link details in the SPARQL store.
   *
   * @param link Link to be updated.
   */
  private void updateStore(Link link) {
    log.info("*** Updating link " + link.getHref() + " to store...");
    String sparql = templater.apply("update-links", link.getTemplateDataMap());
    HttpMessage message = sparqlConnector.update(
      config.getProperty("UPDATE_ENDPOINT"),
      sparql
    );
    message.setRequestBody(sparql);
  }

  /**
   * Retrieves links from the SPARQL store.
   *
   * @return Set of links retrieved from the store.
   */
  private Set<Link> getLinksFromStore() {
    String sparqlLocation = config.getProperty("GET_LINKS_SPARQL_LOCATION");
    String sparql = textFileReader.read(sparqlLocation);
    SparqlResultsParser parser = new SparqlResultsParser();
    parser.setSparql(sparql);
    String xmlResults = sparqlConnector.query(
      config.getProperty("QUERY_ENDPOINT"),
      sparql
    );
    List<Result> results = parser.parse(xmlResults).getResults();
    Set<Link> links = new HashSet<Link>();
    for (Result result : results) {
      Link link = new LinkImpl();
      Iterator<Binding> iterator = result.iterator();
      while (iterator.hasNext()) {
        Binding binding = iterator.next();
        setValue(link, binding.getName(), binding.getValue());
      }
      links.add(link);
    }
    return links;
  }

  /**
   * Sets the value of a specific property of the link.
   *
   * @param link Link object to be updated.
   * @param name Name of the property.
   * @param value Value of the property.
   */
  private void setValue(Link link, String name, String value) {
    if ("origin".equals(name)) {
      link.setOrigin(value);
    }
    if ("href".equals(name)) {
      link.setHref(value);
    }
    if ("label".equals(name)) {
      link.setLabel(value);
    }
    if ("rel".equals(name)) {
      link.setRel(value);
    }
    if ("responseCode".equals(name)) {
      link.setResponseCode(Integer.parseInt(value));
    }
    if ("contentType".equals(name)) {
      link.setContentType(value);
    }
    if ("format".equals(name)) {
      link.setFormat(value);
    }
    if ("explored".equals(name)) {
      link.setExplored("true".equals(value));
    }
    if ("remote".equals(name)) {
      link.setRemote("true".equals(value));
    }
    if ("relevance".equals(name)) {
      link.setRelevance(value.charAt(0));
    }
  }
}

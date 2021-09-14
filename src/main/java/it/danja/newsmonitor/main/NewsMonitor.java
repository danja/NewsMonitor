/**
 * NewsMonitor
 *
 * NewsMonitor.java
 *
 * @author danja
 * @date Apr 23, 2014
 *
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.discovery.LinkExplorer;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.sparql.SparqlTemplater;
import it.danja.newsmonitor.standalone.templating.FsTemplateLoader;
import it.danja.newsmonitor.templating.TemplateLoader;
import it.danja.newsmonitor.templating.Templater;

import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NewsMonitor {

    private static Logger log = LoggerFactory.getLogger(NewsMonitor.class);

    private LinkExplorer linkExplorer;
    private Poller poller;

    private BundleContext bundleContext = null;

    private Properties config = null;

    private Templater templater = null;

    private TextFileReader textFileReader = null;

    /**
     * Constructor
     */
    public NewsMonitor(Properties config, TextFileReader textFileReader, Templater templater) {
        this.templater = templater;
        this.config = config;
        this.textFileReader = textFileReader;
    }

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public void start() {

        System.out.println("START()");

        // log.debug(bundleContext.toString());
        SystemStatus status = new SystemStatus(config, textFileReader, templater);

        status.initializeFeedListFromFile();

        SparqlTemplater sparqlTemplater = new SparqlTemplater(config, textFileReader, templater);
        // load seed list from file into store
        poller = new Poller(config, sparqlTemplater);

        // load feed list from store into memory, pass to Poller
        log.info("Loading feed list from store...");
        poller.setFeedUrls(getFeeds());

        // FeedList feedSet =
        log.info("==== Initialising Feeds ====");
        poller.initFeeds();

        linkExplorer = new LinkExplorer(config, poller.getFeedList(), textFileReader, templater);

        log.info("==== Starting Poller ====");
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

    public void stop() {
        poller.stop();
        linkExplorer.stop();
    }

    private List<String> getFeeds() {
        FeedUrls feedUrlList = new FeedUrls(config, textFileReader);

        String feedlistLocation = config.getProperty("SPARQL_FEEDLIST_LOCATION");
        System.out.println("feedlistLocation = " + feedlistLocation);
        feedUrlList.load(feedlistLocation);
        return feedUrlList.getFeeds();
    }
}

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
import it.danja.newsmonitor.templating.Templater;

import java.util.List;
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
    
	/**
	 * Constructor for standalone build
	 */
    public NewsMonitor(){
    }
    
	/**
	 * When created within OSGi, the BundleContext is passed along
	 */
    public NewsMonitor(BundleContext bundleContext){
    	this.bundleContext = bundleContext;
    }

  //  private BundleContext bundleContext;

//    public void setBundleContext(BundleContext bundleContext) {
//        this.bundleContext = bundleContext;
//    }
//
//    public BundleContext getBundleContext() {
//        return bundleContext;
//    }

    /**
     * @param args
     */
    public void start() {
        start(Config.SEED_FEEDLIST);
    }

    /**
     * @param args
     */
    public void start(String feedlistFilename) {

        Templater templater = new Templater(bundleContext);
        templater.init();

        SystemStatus status = new SystemStatus(bundleContext);


        status.initializeFeedListFromFile(feedlistFilename);

		// load seed list from file into store
        poller = new Poller(bundleContext);

        // load feed list from store into memory, pass to Poller
        log.info("Loading feed list from store...");
        poller.setFeedUrls(getFeeds());

        // FeedList feedSet =
        log.info("==== Initialising Feeds ====");
        poller.initFeeds();

        linkExplorer = new LinkExplorer(poller.getFeedList());
        linkExplorer.setBundleContext(bundleContext);
        
        log.info("==== Starting Poller ====");
        poller.start();
        linkExplorer.start();

        if (Config.TEST_RUN > 0) {
            try {
                Thread.sleep(Config.TEST_RUN * 60000); // wait a bit
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
            poller.displayFeeds();
            log.info("\n==== Stopped Poller ====");
            while (!poller.isStopped() || !linkExplorer.isStopped()) {
                try {
                    Thread.sleep(1000); // wait a bit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        poller.stop();
        linkExplorer.stop();
    }

    private List<String> getFeeds() {
        FeedUrls feedUrlList = new FeedUrls();
        feedUrlList.setBundleContext(bundleContext);
        feedUrlList.load();
        return feedUrlList.getFeeds();
    }
}
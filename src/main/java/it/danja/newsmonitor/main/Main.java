/**
 * NewsMonitor
 *
 * Main.java
 * @author danja
 * @date Apr 23, 2014
 *
 */
package it.danja.newsmonitor.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.discovery.LinkExplorer;
import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.FeedListLoader.LineHandler;
import it.danja.newsmonitor.templating.Templater;

import java.util.List;

/**
 *
 */
public class Main implements Runnable {

	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static final boolean POLLER_NO_LOOP = false; // for debugging
	private static LinkExplorer linkExplorer;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		Templater.init();
		
//		
//		String sparqlBootstrap = TextFileReader.read(Config.BOOTSTRAP_SPARQL);
//		SparqlConnector.update(Config.UPDATE_ENDPOINT, sparqlBootstrap);

		
		SystemStatus status = new SystemStatus();
	//	log.info("POLLER RUNNING = "+status.getPollerRunning());
		// Config.load();

		status.initializeFeedListFromFile(Config.SEED_FEEDLIST);
		
		if(args.length > 1) {
			log.info("args[0] = "+args[0]);
			if("-C".equals(args[0])) {
				status.initializeFeedListFromFile(Config.SEED_FEEDLIST);
			} 
			if("-f".equals(args[0])) {
				status.initializeFeedListFromFile(args[1]);
			} 
		}
		// load seed list from file into store

		
		// TODO doesn't appear to be refreshing

		Poller poller = new Poller();

		// load feed list from store into memory, pass to Poller
		log.info("Loading feed list from store...");
		poller.setFeedUrls(main.getFeeds());

		// Set channelURIs = planet.loadChannelList("input/bloggers.rdf");
		// Set channelURIs = planet.loadChannelList("input/feedlist.opml");

		// FeedList feedSet =
		log.info("==== Initialising Feeds ====");
		poller.initFeeds();
		
		linkExplorer = new LinkExplorer(poller.getFeedList());

		log.info("==== Starting Poller ====");
		poller.start();
		linkExplorer.start();
		
		if(Config.TEST_RUN > 0) {
		try {
			Thread.sleep(Config.TEST_RUN*60000); // wait a bit
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 poller.stop();
		 linkExplorer.stop();
		 poller.displayFeeds();
		 log.info("\n==== Stopped Poller ====");
		 while(!poller.isStopped() || !linkExplorer.isStopped()) {
				try {
					Thread.sleep(1000); // wait a bit
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		 }
		}
	}

	private List<String> getFeeds() {
		FeedUrls feedUrlList = new FeedUrls();
		feedUrlList.load();
		return feedUrlList.getFeeds();
	}



	@Override
	public void run() {
		
	}

}

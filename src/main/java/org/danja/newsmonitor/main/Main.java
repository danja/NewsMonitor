/**
 * feedreader-prototype
 *
 * Main.java
 * @author danja
 * @date Apr 23, 2014
 *
 */
package org.danja.newsmonitor.main;

import java.util.List;

import org.danja.newsmonitor.discovery.LinkExplorer;
import org.danja.newsmonitor.io.SparqlConnector;
import org.danja.newsmonitor.io.TextFileReader;
import org.danja.newsmonitor.main.FeedListLoader.LineHandler;
import org.danja.newsmonitor.templating.Templater;

/**
 *
 */
public class Main implements Runnable {

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
	//	System.out.println("POLLER RUNNING = "+status.getPollerRunning());
		// Config.load();

		status.initializeFeedListFromFile(Config.SEED_FEEDLIST);
		
		if(args.length > 1) {
			System.out.println("args[0] = "+args[0]);
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
		System.out.println("Loading feed list from store...");
		poller.setFeedUrls(main.getFeeds());

		// Set channelURIs = planet.loadChannelList("input/bloggers.rdf");
		// Set channelURIs = planet.loadChannelList("input/feedlist.opml");

		// FeedList feedSet =
		System.out.println("==== Initialising Feeds ====");
		poller.initFeeds();
		
		linkExplorer = new LinkExplorer(poller.getFeedList());

		System.out.println("==== Starting Poller ====");
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
		 System.out.println("\n==== Stopped Poller ====");
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

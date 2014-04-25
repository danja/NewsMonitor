/**
 * feedreader-prototype
 *
 * Main.java
 * @author danja
 * @date Apr 23, 2014
 *
 */
package org.danja.feedreader.main;

import java.util.List;
import java.util.Set;

import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.feeds.FeedSet;
import org.danja.feedreader.io.FileEntrySerializer;
import org.danja.feedreader.io.SparqlConnector;
import org.danja.feedreader.main.FeedListLoader.LineHandler;

/**
 *
 */
public class Main {

	public static final boolean POLLER_NO_LOOP = false; // for debugging

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		Configuration.load();

		// load seed list from file into store
		main.loadSeedFeedList();
		
		// TODO doesn't appear to be refreshing

		Poller poller = new Poller();

		// load feed list from store into memory, pass to Poller
		poller.setFeedList(main.getFeeds());

		// Set channelURIs = planet.loadChannelList("input/bloggers.rdf");
		// Set channelURIs = planet.loadChannelList("input/feedlist.opml");

		// FeedSet feedSet =
		poller.initFeeds();

		poller.start();
//		try {
//			Thread.sleep(60000); // wait a minute
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		// poller.stop();
	}

	private List<String> getFeeds() {
		FeedList feedList = new FeedList();
		feedList.load();
		return feedList.getFeeds();
	}

	public void loadSeedFeedList() {
		FeedListLoader loader = new FeedListLoader();
		LineHandler handler = loader.new LineHandler();
		// TODO move to config
		String turtleBody = loader.readFile("input/short-list.txt", // buggy-list.txt rdf-bloggers-feedlist.txt
				handler);
		String sparql = FeedListLoader.insertValue(
				FeedListLoader.SPARQL_TEMPLATE, "channels", turtleBody);
		// System.out.println("Query = \n" + sparql);
		int responseCode = SparqlConnector.update(
				"http://localhost:3030/feedreader/update", sparql);
		// System.out.println(responseCode);
	}

}
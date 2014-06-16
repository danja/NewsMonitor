/**
 * feedreader-prototype
 *
 * Poller.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.main;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.danja.feedreader.interpreters.FormatSniffer;
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.HttpMessage;
import org.danja.feedreader.model.EntryList;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.FeedList;
import org.danja.feedreader.model.impl.EntryListImpl;
import org.danja.feedreader.model.impl.FeedImpl;
import org.danja.feedreader.model.impl.FeedListImpl;
import org.danja.feedreader.sparql.SparqlTemplater;

public class Poller implements Runnable {

	// private EntryList entries = new EntryListImpl();

	private List<String> feedUrls = null;

	private FeedList feedList = new FeedListImpl();

	/**
	 * @return the feedList
	 */
	public FeedList getFeedList() {
		return feedList;
	}

	private boolean running = false;

	private Thread thread;

	private int loopCount = 0;

	private boolean stopped = false;

	/**
	 * Takes each feed URL on the list and using @see FormatSniffer checks the
	 * target type, then creates @see Feed objects according to what it finds,
	 * adding these to the @see FeedList
	 * 
	 * @return
	 */
	public FeedList initFeeds() {
		for (int i = 0; i < feedUrls.size(); i++) {
			Feed feed = new FeedImpl();
			feed.setUrl(feedUrls.get(i));
			feed.init();
			feedList.addFeed(feed);
		}
		return feedList;
	}

	public void start() {
		stopped = false;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		running = true;
		feedList.setFirstCall(true);
		// System.out.println("FEEDLIST = " + feedList);
		while (running) {
			if(feedList.size() == 0) {
				System.out.println("No valid feeds, stopping poller...");
				running = false;
				break;
			}
			System.out.println("\n*** Starting loop #" + (++loopCount) +" ***");
			System.out.println("Refreshing " + feedList.size() + " feeds...");

			feedList.setFirstCall(false);
			feedList.refreshAll();
			// displayFeeds();
			pushFeeds();
		}
		System.out.println("Poller stopped.");
		stopped = true;
	}

	private void pushFeeds() {
		ConcurrentLinkedQueue<Feed> feeds = feedList.getList();
		//for (int i = 0; i < feeds.size(); i++) {
		//	Feed feed = feeds.get(i);Listterator
		Iterator<Feed> iterator = feeds.iterator();
			while(iterator.hasNext()) {
				Feed feed = iterator.next();
			if (feed.isNew()) {
				System.out.println("Uploading SPARQL for : " + feed.getUrl());
				HttpMessage message = SparqlTemplater.uploadFeed(feed);
				feed.clean();
				System.out.println("SPARQL response = " + message.getStatusCode()+ " " +message.getStatusMessage());
				if(message.getStatusCode() >= 400) {
					System.out.println("\n"+message+"\n");
				}
			} else {
				System.out.println("No changes to : " + feed.getUrl());
			}
		}
	}

	public void displayFeeds() {
		Iterator<Feed> feedIterator = feedList.getList().iterator();
		while (feedIterator.hasNext()) {
			System.out.println(feedIterator.next());
		}
		System.out.println("---------------");
		// for (int i = 0; i < entries.size(); i++) {
		// System.out.println(entries.getEntry(i));
		// }
	}

	public void setFeedUrls(List<String> feedUrls) {
		this.feedUrls = feedUrls;
	}

	public boolean isStopped() {
		return stopped;
	}
}
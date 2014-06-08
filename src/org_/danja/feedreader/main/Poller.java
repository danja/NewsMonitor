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

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.feeds.impl.EntryListImpl;
import org.danja.feedreader.feeds.impl.FeedImpl;
import org.danja.feedreader.feeds.impl.FeedListImpl;
import org.danja.feedreader.interpreters.FormatSniffer;
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.HttpMessage;
import org.danja.feedreader.sparql.SparqlTemplater;

public class Poller implements Runnable {

//	private EntryList entries = new EntryListImpl();

	private List<String> feedUrls = null;

	private FeedList feedList = new FeedListImpl();

	private boolean running = false;

	private Thread thread;

	private int loopCount = 0;

	/**
	 * Takes each feed URL on the list and using @see FormatSniffer checks the
	 * target type, then creates @see Feed objects according to what it finds,
	 * adding these to the @see FeedList
	 * 
	 * @return
	 */
	public FeedList initFeeds() {
		System.out.println("Poller.initFeeds()");
		Feed feed;
		Interpreter interpreter;
		String url;
		FormatSniffer sniffer = new FormatSniffer();
		HttpConnector connector;
		char format;

		// System.out.println("feedUrls.size() = " + feedUrls.size());
		for (int i = 0; i < feedUrls.size(); i++) {

			// check the target URL
			url = feedUrls.get(i);
			connector = new HttpConnector();
			connector.setUrl(url);
			connector.setConditional(false);
			System.out.println("\n\nInitializing : " + url);

			boolean streamAvailable = connector.load();
			if (streamAvailable) {
				System.out.println("Sniffing...");
				format = sniffer.sniff(connector.getInputStream());
				// System.out.println("===Headers ===\n"+connector.getHeadersString()+"------\n");
			} else {
				System.out.println("Stream unavailable.");
				format = FeedConstants.UNKNOWN;
			}
			System.out.println("Format matches : "
					+ FeedConstants.formatName(format));
			System.out.println("\nCreating object for feed : " + url);

			// create Feed object
			feed = new FeedImpl();
			feed.setUrl(url);
			feed.setFormatHint(format); // TODO remove duplication with setInterpreter
			// feed.setRefreshPeriod(Config.getPollerPeriod());

			// interpreter = RDFInterpreterFactory.createInterpreter(format);
			// feed.setInterpreter(interpreter);

			interpreter = InterpreterFactory.createInterpreter(feed);
			
			System.out.println("Setting interpreter " + interpreter
					+ " to feed " + url);
			feed.setInterpreter(interpreter);

			feedList.addFeed(feed);
		}
		return feedList;
	}

	public void start() {
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
			System.out.println("Starting loop #" + ++loopCount);

			System.out.println("Refreshing " + feedList.size() + " feeds...");

			feedList.setFirstCall(false);

			feedList.refreshAll();
		//	System.out.println("A");
			displayFeeds();
		//	System.out.println("B");
			pushFeeds();
		//	System.out.println("C");
		}
		System.out.println("Poller stopped.");
	}

	private void pushFeeds() {
		List<Feed> feeds = feedList.getList();
		for(int i=0;i<feeds.size();i++){
			System.out.println("Uploading : "+feeds.get(i).getUrl());
			HttpMessage message = SparqlTemplater.uploadFeed(feeds.get(i));
			System.out.println("response = "+message);
		}
	}

	public void displayFeeds() {
		Iterator<Feed> feedIterator = feedList.getList().iterator();
		while (feedIterator.hasNext()) {
			System.out.println(feedIterator.next());
		}
		System.out.println("---------------");
//		for (int i = 0; i < entries.size(); i++) {
//			System.out.println(entries.getEntry(i));
//		}
	}

	public void setFeedList(List<String> feedUrls) {
		this.feedUrls = feedUrls;
	}
}
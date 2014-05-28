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
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.parsers.FormatSniffer;

public class Poller implements Runnable {

	private EntryList entries = new EntryListImpl();

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
			feed.setRefreshPeriod(Config.getPollerPeriod());

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
		while (running) {
			System.out.println("Starting loop #" + ++loopCount);

			System.out.println("Refreshing " + feedList.size() + " feeds...");

			feedList.setFirstCall(false);

			feedList.refreshAll();

			// System.out.println("*** STATUS ***");
			// displayStatus(feedList);

			// local display of recent entries
			entries = feedList.getEntries();

			System.out.println("FEEDLIST = " + feedList);
			System.out.println("DISPLAY entries.size() = " + entries.size());

			// TODO sort entries
			entries.trimList(Config.getMaxItems());

			/*
			FileEntrySerializer serializer = new FileEntrySerializer();
			serializer.loadDocumentShell("input/shell.xml"); // TODO move to
																// Config

			serializer.clearEntries();
			System.out.println("entries.size() = " + entries.size());
			for (int i = 0; i < entries.size(); i++) {
				serializer.addEntry(entries.getEntry(i));
			}
			System.out.println("Writing RSS 2.0...");
			serializer.write("output/rss.xml");
			System.out.println("Writing HTML...");
			serializer
					.transformWrite("output/index.html", "xslt/rss2html.xslt");
			System.out.println("Writing RSS 1.0...");
			serializer
					.transformWrite("output/feed.rdf", "xslt/feed-rss1.0.xsl");
					*/
		}
		System.out.println("Poller stopped.");
	}

	public void displayStatus(FeedList feeds) {
		Iterator feedIterator = feeds.getFeedCollection().iterator();
		while (feedIterator.hasNext()) {
			System.out.println(((Feed) feedIterator.next()).getStatus());
		}
		System.out.println("---------------");
		for (int i = 0; i < entries.size(); i++) {
			System.out.println(entries.getEntry(i));
		}
	}

	public void setFeedList(List<String> feeds) {
		feedUrls = feeds;
	}
}
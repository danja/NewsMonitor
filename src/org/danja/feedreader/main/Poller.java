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
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedImpl;
import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.feeds.FeedListImpl;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.io.FileEntrySerializer;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.parsers.FormatSniffer;

public class Poller implements Runnable {

	private EntryList entries = new EntryListImpl(); 

	private List<String> feedUrls = null;

	private FeedList feedList = new FeedListImpl();

	private boolean running = false;

	private Thread thread;
	
	private int loopCount = 0;

	/**
	 * Takes each feed URL on the list and using @see FormatSniffer checks the target type,
	 * then creates @see Feed objects according to what it finds, adding these to the @see FeedList
	 * @return
	 */
	public FeedList initFeeds() {
		Feed feed;
		Interpreter interpreter;
		String url;
		FormatSniffer sniffer = new FormatSniffer();
		HttpConnector connector;
		char format;

		System.out.println("feedUrls.size() = " + feedUrls.size());
		for (int i = 0; i < feedUrls.size(); i++) {
			
			// check the target URL
			url = feedUrls.get(i);
			connector = new HttpConnector();
			connector.setUrl(url);
			boolean streamAvailable = connector.load();
			if (streamAvailable) {
				System.out.println("Sniffing feed : "+url);
				format = sniffer.sniff(connector.getInputStream());
			} else {
				System.out.println("Stream unavailable.");
				format = FeedConstants.UNKNOWN;
			}
			System.out.println("Format matches : " + FeedConstants.formatName(format));
			System.out.println("\nCreating object for feed : " + url);
			
			// create Feed object
			feed = new FeedImpl();
			feed.setUrl(url);
			feed.setFormatHint(format);
			feed.setRefreshPeriod(Config.getPollerPeriod()); 
			
			interpreter = InterpreterFactory.createInterpreter(format);
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
		while (running) {
			System.out.println("Starting loop #"+ ++loopCount);
			
			System.out.println("BEFORE feedList.size() "+feedList.size());	
			feedList.refreshAll();
			System.out.println("AFTER feedList.size() "+feedList.size());
			System.out.println(feedList);
			displayStatus(feedList);
			System.out.println("1entries.size() = " + entries.size());
			entries.trimList(Config.getMaxItems());

			FileEntrySerializer serializer = new FileEntrySerializer();
			serializer.loadDocumentShell("input/shell.xml");

			serializer.clearEntries();
			System.out.println("entries.size() = " + entries.size());
			for (int i = 0; i < entries.size(); i++) {
				serializer.addEntry(entries.getEntry(i));
			}
			System.out.println("Writing RSS 2.0...");
			serializer.write("output/rss.xml");
			System.out.println("Writing HTML...");
			serializer.transformWrite("output/index.html",
					"xslt/rss2html.xslt");
			System.out.println("Writing RSS 1.0...");
			serializer.transformWrite("output/feed.rdf",
					"xslt/feed-rss1.0.xsl");
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
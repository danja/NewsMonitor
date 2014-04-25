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
import java.util.Set;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedImpl;
import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.feeds.FeedListImpl;
import org.danja.feedreader.io.FileEntrySerializer;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.parsers.InterpreterFactory;
import org.danja.feedreader.social.FormatSniffer;
import org.danja.feedreader.social.RDFInterpreterFactory;

public class Poller implements Runnable {

	private EntryList entries = new EntryListImpl(); // ++

	private List<String> feedUrls = null;

	private FeedList feedSet = new FeedListImpl();

	private boolean running = false;

	private Thread thread;

	public FeedList initFeeds() {
		Feed feed;
		Interpreter interpreter;
		String url;
		FormatSniffer sniffer = new FormatSniffer();
		HttpConnector connector;
		char format;

		System.out.println("feedUrls.size() = " + feedUrls.size());
		for (int i = 0; i < feedUrls.size(); i++) {
			url = feedUrls.get(i);
			System.out.println("A URL = "+url);
			connector = new HttpConnector(url);
			boolean streamAvailable = connector.load();
			if (streamAvailable) {
				format = sniffer.sniff(connector.getInputStream());
			} else {
				System.out.println("Stream unavailable.");
				format = FeedConstants.UNKNOWN;
			}
			System.out.println("\nFeed : " + url);
			System.out.println("Format : " + FeedConstants.formatName(format));

			feed = new FeedImpl(url);
			feed.setFormatHint(format);
			interpreter = RDFInterpreterFactory.createInterpreter(format,
					entries);
			feed.setInterpreter(interpreter);
			feed.setRefreshPeriod(Configuration.getPollerPeriod()); // TODO move
			System.out.println("feed URL = " + feed.getUrl());
			feedSet.addFeed(feed);
		}
		return feedSet;
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
		System.out.println("Poller started.");
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			System.out.println("RUNNING!");
			feedSet.refreshAll();
			displayStatus(feedSet);
			System.out.println("1entries.size() = " + entries.size());
			entries.trimList(Configuration.getMaxItems());

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
					"templates/rss2html.xslt");
			System.out.println("Writing RSS 1.0...");
			serializer.transformWrite("output/feed.rdf",
					"templates/feed-rss1.0.xsl");
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
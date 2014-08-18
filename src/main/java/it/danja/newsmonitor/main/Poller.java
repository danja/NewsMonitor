/**
 * NewsMonitor
 *
 * Poller.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.interpreters.FormatSniffer;
import it.danja.newsmonitor.interpreters.Interpreter;
import it.danja.newsmonitor.interpreters.InterpreterFactory;
import it.danja.newsmonitor.io.HttpConnector;
import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.model.EntryList;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedList;
import it.danja.newsmonitor.model.impl.EntryListImpl;
import it.danja.newsmonitor.model.impl.FeedImpl;
import it.danja.newsmonitor.model.impl.FeedListImpl;
import it.danja.newsmonitor.sparql.SparqlTemplater;
import it.danja.newsmonitor.utils.ContentType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Poller implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(Poller.class);

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
		// feedList.setFirstCall(true);
		// log.info("FEEDLIST = " + feedList);
		while (running) {
			if (feedList.size() == 0) {
				log.info("No valid feeds, stopping poller...");
				running = false;
				break;
			}
			System.out
					.println("\n*** Starting loop #" + (++loopCount) + " ***");
			log.info("Refreshing " + feedList.size() + " feeds...");

			// feedList.setFirstCall(false);
			refreshFeeds();
			// displayFeeds();
			// pushFeeds();
		}
		log.info("Poller stopped.");
		stopped = true;
	}

	public synchronized void refreshFeeds() {
		Set<Feed> expiring = new HashSet<Feed>();
		Iterator<Feed> iterator = feedList.getList().iterator();
		// feedQueue.iterator();
		Feed feed;

		while (iterator.hasNext()) {
			feed = iterator.next();
			// log.info("feed.getLives() = "+feed.getLives());
			// log.info("feed.isDead() = "+feed.isDead());
			// if(!feed.isDead() && feed.getLives() < Config.MAX_LIVES) {
			// log.info("Less than max lives, re-initializing...");
			// feed.init();
			// // feed.setFirstCall(true);
			// }
			log.info("\nRefreshing : " + feed.getUrl());
			// feed.setFirstCall(firstCall);
			feed.refresh();

			if (feed.getFormatHint() == ContentType.HTML) { // shouldn't be
															// needed
				feed.setDead(true);
				log.info("Is HTML...");
			}
			if (feed.getLives() < 1) {
				log.info("Lives gone...");
				feed.setDead(true);
			}
			if (feed.getRelevance() < Config.UNSUBSCRIBE_RELEVANCE_THRESHOLD) {
				log.info("Now below relevance threshold...");
				feed.setDead(true);
			}
			if (feed.isDead()) {
				log.info("Flagging as dead, skipping.");

				// TODO is duplicated below
				log.info("Unsubscribing from " + feed.getUrl());
				feedList.remove(feed);
				continue;
			}
			;

			if (feed.shouldExpire()) {
				expiring.add(feed);
			}
			try {
				Thread.sleep(Config.PER_FEED_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (feed.isNew()) {
				pushFeed(feed);
			} else {
				log.info("No changes to : " + feed.getUrl());
			}
		}

		if (Config.POLLER_NO_LOOP) {
			System.exit(1);
		}

		iterator = expiring.iterator();
		while (iterator.hasNext()) {
			feed = iterator.next();
			log.info("Unsubscribing from " + feed.getUrl());
			feedList.remove(feed);
		}

		try {
			Thread.sleep(Config.REFRESH_PERIOD);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void pushFeed(Feed feed) {

		log.info("Uploading SPARQL for : " + feed.getUrl());
		HttpMessage message = SparqlTemplater.uploadFeed(feed);
		feed.clean();
		log.info("SPARQL response = " + message.getStatusCode() + " "
				+ message.getStatusMessage());
		if (message.getStatusCode() >= 400) {
			log.info("\n" + message + "\n");
		}

	}

	// private void pushFeeds() {
	// ConcurrentLinkedQueue<Feed> feeds = feedList.getList();
	// //for (int i = 0; i < feeds.size(); i++) {
	// // Feed feed = feeds.get(i);Listterator
	// Iterator<Feed> iterator = feeds.iterator();
	// while(iterator.hasNext()) {
	// Feed feed = iterator.next();
	// if (feed.isNew()) {
	// log.info("Uploading SPARQL for : " + feed.getUrl());
	// HttpMessage message = SparqlTemplater.uploadFeed(feed);
	// feed.clean();
	// log.info("SPARQL response = " + message.getStatusCode()+ " "
	// +message.getStatusMessage());
	// if(message.getStatusCode() >= 400) {
	// log.info("\n"+message+"\n");
	// }
	// } else {
	// log.info("No changes to : " + feed.getUrl());
	// }
	// }
	// }

	public void displayFeeds() {
		Iterator<Feed> feedIterator = feedList.getList().iterator();
		while (feedIterator.hasNext()) {
			log.info(feedIterator.next().toString());
		}
		log.info("---------------");
		// for (int i = 0; i < entries.size(); i++) {
		// log.info(entries.getEntry(i));
		// }
	}

	public void setFeedUrls(List<String> feedUrls) {
		this.feedUrls = feedUrls;
	}

	public boolean isStopped() {
		return stopped;
	}
}
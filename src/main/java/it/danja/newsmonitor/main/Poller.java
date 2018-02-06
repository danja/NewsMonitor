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

import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedList;
import it.danja.newsmonitor.model.impl.FeedImpl;
import it.danja.newsmonitor.model.impl.FeedListImpl;
import it.danja.newsmonitor.sparql.SparqlTemplater;
import it.danja.newsmonitor.utils.ContentType;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Poller implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(Poller.class);

	private List<String> feedUrls = null;

	private FeedList feedList = null;

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
	
	SparqlTemplater sparqlTemplater = null;

	private Properties config = null;
	
// private BundleContext bundleContext;
	
//	public void setBundleContext(BundleContext bundleContext) {
//		this.bundleContext = bundleContext;
//	}
	
	public Poller(Properties config, SparqlTemplater sparqlTemplater) {
		this.sparqlTemplater = sparqlTemplater;
		this.config  = config;
                System.out.println(config);
		feedList = new FeedListImpl(config);
		// sparqlTemplater.setBundleContext(bundleContext);
	}

	/**
	 * Takes each feed URL on the list and using @see FormatSniffer checks the
	 * target type, then creates @see Feed objects according to what it finds,
	 * adding these to the @see FeedList
	 * 
	 * @return
	 */
	public FeedList initFeeds() {
		for (int i = 0; i < feedUrls.size(); i++) {
			Feed feed = new FeedImpl(config);
			feed.setUrl(feedUrls.get(i));
			boolean redirect = feed.init();
                        if(redirect){
                            String location = feed.getLocation();
                            feed = new FeedImpl(config);
                            feed.setUrl(location);
                            feed.init();
                        }
			feedList.addFeed(feed);
		}
                 log.info("==== FeedList ====");
                 log.info(feedList.toString());
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
				log.warn("No valid feeds, stopping poller...");
				running = false;
				break;
			}
			log.info("\n*** Starting loop #" + (++loopCount) + " ***");
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
			// feed.setDead(true);
				log.info("Is HTML...");
			}
			if (feed.getLives() < 1) {
				log.info("Lives gone...");
				feed.setDead(true);
			}
			if (feed.getRelevance() < Float.parseFloat(config.getProperty("UNSUBSCRIBE_RELEVANCE_THRESHOLD"))) {
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
				Thread.sleep(Integer.parseInt(config.getProperty("PER_FEED_SLEEP_PERIOD")));
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}

			if (feed.isNew()) {
				pushFeed(feed);
			} else {
				log.info("No changes to : " + feed.getUrl());
			}
		}

		if ("true".equals(config.getProperty("POLLER_NO_LOOP"))) {
			System.exit(1);
		}

		iterator = expiring.iterator();
		while (iterator.hasNext()) {
			feed = iterator.next();
			log.info("Unsubscribing from " + feed.getUrl());
			feedList.remove(feed);
		}

		try {
			Thread.sleep(Integer.parseInt(config.getProperty("REFRESH_PERIOD")));
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}

	private void pushFeed(Feed feed) {

		log.info("Uploading SPARQL for : " + feed.getUrl());
		HttpMessage message = sparqlTemplater.uploadFeed(feed);
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
                
                List<String> tweakedUrls = new ArrayList<String>(); // little temp workaround for some old feeds
                for(int i=0;i<feedUrls.size();i++){
                    String url = feedUrls.get(i);
                    if(url.startsWith("http:")){
                        String tweaked = "https:" +url.substring(5);
                        log.info("tweaked = "+tweaked);
                        tweakedUrls.add(tweaked);
                    }
                }
                this.feedUrls.addAll(tweakedUrls);
	}

	public boolean isStopped() {
		return stopped;
	}
}
/**
 * TODO revisit
 * 
 * feedreader-prototype
 *
 * FeedListImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.danja.feedreader.main.Config;
import org.danja.feedreader.main.Main;
import org.danja.feedreader.model.EntryList;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.FeedList;
import org.danja.feedreader.model.Link;

/**
 * Implements set of feeds
 * 
 * 
 */
public class FeedListImpl implements FeedList {

	private ConcurrentLinkedQueue<Feed> feedQueue;
	// private EntryList entries;
	// private boolean firstCall;

	public FeedListImpl() {
		feedQueue = new ConcurrentLinkedQueue<Feed>();
	}

	public Feed createFeed(String url) {
		Feed feed = new FeedImpl();
		feed.setUrl(url);
		return feed;
	}

	public void addFeed(String uriString) {
		addFeed(uriString, ContentType.UNKNOWN);
	}

	public void addFeed(Feed feed) {
		// feedQueue.addFirst(feed); // it's shiny new and interesting
		feedQueue.add(feed);
	}

	public void addFeed(String uriString, char formatHint) {
		Feed feed = createFeed(uriString);
		feed.setFormatHint(formatHint);
		addFeed(feed);
	}

	public void addFeeds(FeedList feeds) {
		feedQueue.addAll(feeds.getList());
	}

	public ConcurrentLinkedQueue<Feed> getList() {
		return feedQueue;
	}

	public Feed getNext() {
		Feed next = feedQueue.poll();
	//	Feed next = (Feed) feedQueue.removeFirst();
	//	feedQueue.addLast(next);
		return next;
	}

	public synchronized void refreshAll() {
		Set<Feed> expiring = new HashSet<Feed>();
		Iterator<Feed> iterator = feedQueue.iterator();
		Feed feed;
		

		while (iterator.hasNext()) {
			feed = iterator.next();
//			System.out.println("feed.getLives() = "+feed.getLives());
//			System.out.println("feed.isDead() = "+feed.isDead());
//			if(!feed.isDead() && feed.getLives() < Config.MAX_LIVES) {
//				System.out.println("Less than max lives, re-initializing...");
//				feed.init();
//				// feed.setFirstCall(true);
//			}
			System.out.println("\nRefreshing : " + feed.getUrl());
			// feed.setFirstCall(firstCall);
			feed.refresh();
			
			if(feed.getFormatHint() == ContentType.HTML) { // shouldn't be needed
				feed.setDead(true);
				System.out.println("Is HTML...");
			}
			if(feed.getLives() < 1) {
				System.out.println("Lives gone...");
				feed.setDead(true);
			}
			if(feed.getRelevance() < Config.UNSUBSCRIBE_RELEVANCE_THRESHOLD) {
				System.out.println("Now below relevance threshold...");
				feed.setDead(true);
			}
			if(feed.isDead()) {
				System.out.println("Flagging as dead, skipping.");
				
				// TODO is duplicated below
				System.out.println("Unsubscribing from " + feed.getUrl());
				feedQueue.remove(feed);
				continue;
			};

			if (feed.shouldExpire()) {
				expiring.add(feed);
			}
			try {
				Thread.sleep(Config.PER_FEED_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		if (Main.POLLER_NO_LOOP) {
			System.exit(1);
			;
		}
		iterator = expiring.iterator();
		while (iterator.hasNext()) {
			feed = iterator.next();
			System.out.println("Unsubscribing from " + feed.getUrl());
			feedQueue.remove(feed);
			// TODO pass to SPARQL store
		}
		try {
			Thread.sleep(Config.REFRESH_PERIOD);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		System.out.println("FEEDLIST size() = " + feedQueue.size());
		StringBuffer buffer = new StringBuffer();
		buffer.append("===Feed List===\n");

		Iterator<Feed> iterator = feedQueue.iterator();

		while (iterator.hasNext()) {
			buffer.append(iterator.next().toString());
		}
		return buffer.toString();
	}

	@Override
	public int size() {
		return feedQueue.size();
	}

	// @Override
	// public EntryList getEntries() {
	// return entries;
	// }


	@Override
	public Set<Link> getAllLinks() {
		return getLinks(false);
	}
	
	@Override
	public Set<Link> getRemoteLinks() {
		return getLinks(true);
	}
	
	
	private Set<Link> getLinks(boolean includeLocal) {
		Set<Link> links = new HashSet<Link>();
		Iterator<Feed> iterator = feedQueue.iterator();
		while (iterator.hasNext()) {
			if(includeLocal) {
				links.addAll(iterator.next().getAllLinks());	
			} else {
				links.addAll(iterator.next().getRemoteLinks());
			}
			
		}
		return links;
	}


}
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
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.main.Main;
import it.danja.newsmonitor.model.EntryList;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedList;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.utils.ContentType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implements set of feeds
 * 
 * 
 */
public class FeedListImpl implements FeedList {

	private ConcurrentLinkedQueue<Feed> feedQueue;

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
	
	public boolean containsFeed(String url) { // TODO refactor
		Iterator<Feed> it = feedQueue.iterator();
		while(it.hasNext()){
			if(url.equals(it.next().getUrl())) {
				return true;
			}
		}
		return false;
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

	@Override
	public void remove(Feed feed) {
		feedQueue.remove(feed);
	}


}
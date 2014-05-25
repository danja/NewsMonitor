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
package org.danja.feedreader.feeds.impl;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.main.Main;

/**
 * Implements set of feeds
 * 
 *  
 */
public class FeedListImpl implements FeedList {

    private LinkedList<Feed> feedQueue;
	private EntryList entries;
	private boolean firstCall;

    public FeedListImpl() {
        feedQueue = new LinkedList<Feed>();
    }
    
    public Feed createFeed(String url) {
        Feed feed = new FeedImpl();
        feed.setUrl(url);
        return feed;
    }

    public void addFeed(String uriString) {
        addFeed(uriString, FeedConstants.UNKNOWN);
    }
    
    public void addFeed(Feed feed) {
        feedQueue.addFirst(feed); // it's shiny new and interesting
    }

    public void addFeed(String uriString, char formatHint) {
        Feed feed = createFeed(uriString);
        feed.setFormatHint(formatHint);
        addFeed(feed);
    }

    public void addFeeds(FeedList feeds) {
        feedQueue.addAll(feeds.getFeedCollection());
    }

    public List<Feed> getFeedCollection() {
        return feedQueue;
    }

    public Feed getNext() {
        Feed next = (Feed) feedQueue.removeFirst();
        feedQueue.addLast(next);
        return next;
    }

    public void refreshAll() {
    	System.out.println("Refresh all...");
    	
    	entries = new EntryListImpl();
        Set<Feed> expiring = new HashSet<Feed>();
        Iterator<Feed> iterator = feedQueue.iterator();
        Feed feed;
        while (iterator.hasNext()) {
            feed = iterator.next();
            System.out.println("\n\nRefreshing : " + feed.getUrl());
            feed.setFirstCall(firstCall);
            feed.refresh();
            System.out.println(" feed.getEntries() = "+feed.getEntries());
            entries.addAll(feed.getEntries());
            if(feed.shouldExpire()){
                expiring.add(feed);
            }
            try {
                Thread.sleep(Config.PER_FEED_SLEEP_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(Main.POLLER_NO_LOOP) {
        	System.exit(1);;
        }
        iterator = expiring.iterator();
        while (iterator.hasNext()) {
            feed = iterator.next();
                System.out.println("Unsubscribing from "+feed.getUrl());
                feedQueue.remove(feed);
        }
    }
    
    public String toString() {
    	System.out.println("FEEDLIST size() = "+feedQueue.size());
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

	@Override
	public EntryList getEntries() {
		return entries;
	}

	@Override
	public void setFirstCall(boolean b) {
		this.firstCall = b;
	}
}
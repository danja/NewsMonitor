/**
 * TODO RETIRE
 * feedreader-prototype
 *
 * FeedSetImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.danja.feedreader.main.Configuration;
import org.danja.feedreader.old.FeedSet;

/**
 * Implements set of feeds
 * 
 *  
 */
public class FeedSetImpl implements FeedSet {

    private LinkedList feedQueue;

    public FeedSetImpl() {
        feedQueue = new LinkedList();
    }
    
    public FeedFetcher createFeed(String uri) {
        return new FeedFetcherImpl(uri);
    }

    public void addFeed(String uriString) {
        addFeed(uriString, FeedConstants.UNKNOWN);
    }
    
    public void addFeed(FeedFetcher feed) {
        feedQueue.addFirst(feed); // it's shiny new and interesting
    }

    public void addFeed(String uriString, char formatHint) {
        FeedFetcher feed = createFeed(uriString);
        feed.setFormatHint(formatHint);
        addFeed(feed);
    }

    public void addFeeds(FeedSet feeds) {
        feedQueue.addAll(feeds.getFeedCollection());
    }

    public Collection getFeedCollection() {
        return feedQueue;
    }

    public FeedFetcher getNext() {
        FeedFetcher next = (FeedFetcher) feedQueue.removeFirst();
        feedQueue.addLast(next);
        return next;
    }

    public void refreshAll() {
        Set expiring = new HashSet();
        Iterator iterator = feedQueue.iterator();
        FeedFetcher feed;
        while (iterator.hasNext()) {
            feed = (FeedFetcher) iterator.next();
            System.out.println("\n\nChecking: " + feed.getURIString());
            feed.refresh();
            if(feed.shouldExpire()){
                expiring.add(feed);
            }
            try {
                Thread.sleep(Configuration.PER_FEED_SLEEP_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        iterator = expiring.iterator();
        while (iterator.hasNext()) {
            feed = (FeedFetcher) iterator.next();
                System.out.println("Unsubscribing from "+feed.getURIString());
                feedQueue.remove(feed);
        }
    }
}
/**
 * TODO RETIRE
 * feedreader-prototype
 *
 * FeedSet.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.old;

import java.util.Collection;

import org.danja.feedreader.feeds.FeedFetcher;

/**
 * Models a set of feeds/connections
 * 
 * @version $Revision$
 *  
 */
public interface FeedSet
{

   /**
    * @param uri
    *           URI of feed to add
    */
   public void addFeed(String uriString);

   public void addFeed(String uriString, char formatHint);

   public void addFeed(FeedFetcher feed);

   public void addFeeds(FeedSet feeds);

   /**
    * @return all feeds
    */
   public Collection getFeedCollection();

   public FeedFetcher createFeed(String uri);

   public FeedFetcher getNext();

   public void refreshAll();
}
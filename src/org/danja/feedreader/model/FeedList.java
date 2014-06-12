/**
 * TODO RETIRE
 * feedreader-prototype
 *
 * FeedList.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.model;

import java.util.List;
import java.util.Set;

/**
 * Models a set of feeds/connections
 * 
 * @version $Revision$
 *  
 */
public interface FeedList
{

   /**
    * @param uri
    *           URI of feed to add
    */
   public void addFeed(String uriString);

   public void addFeed(String uriString, char formatHint);

   public void addFeed(Feed feed);

   public void addFeeds(FeedList feeds);

   /**
    * @return all feeds
    */
   public List<Feed> getList();

   public Feed createFeed(String uri);

   public Feed getNext();

   public void refreshAll();
   
   public int size();
   
   public Set<Link> getAllLinks();
   
   public Set<Link> getRemoteLinks();

// public EntryList getEntries();

public void setFirstCall(boolean b);
}
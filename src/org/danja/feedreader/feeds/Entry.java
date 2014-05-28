/**
 * feedreader-prototype
 *
 * Entry.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */

package org.danja.feedreader.feeds;

import java.util.Set;

/**
 * Models an RSS item/Atom entry/single blog post
 */
public interface Entry extends FeedEntity {



    /**
     * @param feedTitle set the title of the feed
     */
  //  public void setSourceTitle(String feedTitle);

    /**
     * @return  get the title of the feed
     */
//    public String getSourceTitle();

    /**
     * @param feedLink set the URL of the feed
     */
 //   public void setSourceLink(String feedLink);

    /**
     * @return  set the URL of the feed
     */
//    public String getSourceLink();
    
    /**
     * Gets an RDF/Turtle representation of the Entry (sans prefixes)
     * 
     * @return the Turtle
     */
    // public String toTurtle();
    
   // public String toTurtleNoPrefixes();

	public void addAllLinks(Set<Link> links);
}
/**
 * NewsMonitor
 *
 * Entry.java
 *
 * @author danja
 * dc:date Apr 25, 2014
 *
 */

package it.danja.newsmonitor.model;

import java.util.Map;
import java.util.Set;

/**
 * Models an RSS item/Atom entry/single blog post
 */
public interface Entry extends FeedEntity {
  public void setFeedUrl(String feedUrl);

  public String getFeedUrl();

  public void setRead(boolean read);

  public boolean isRead();

  /**
   * @param summary
   */
  public void setSummary(String summary);

  /**
   * @return
   */
  public String getSummary();

  public int getContentWordCount();

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
   * return the Turtle
   */
  // public String toTurtle();

  // public String toTurtleNoPrefixes();

  public void addAllLinks(Set<Link> links);
}

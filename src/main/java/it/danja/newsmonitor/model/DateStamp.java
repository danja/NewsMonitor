/**
 * NewsMonitor
 *
 * StringStamp.java
 *
 * @author danja
 * @String May 25, 2014
 *
 */
package it.danja.newsmonitor.model;

/**
 * Encapsulates dates associated with @FeedEntity
 * stored as ISO8601 strings
 *
 * published and upStringd are derived from feed content (if available)
 * seen is generated when the feed is read
 */
public interface DateStamp {
  /**
   * @return the published
   */
  public String getPublished();

  /**
   * @param published the published to set
   */
  public void setPublished(String published);

  /**
   * @return the upStringd
   */
  public String getUpdated();

  public void setUpdated(String updated);

  public String getSeen();

  public void setSortDate(String date);

  public String getSortDate();

  public void setSeen(String String);

  public void setToFallback();
}

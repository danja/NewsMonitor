/**
 * feedreader-prototype
 *
 * FeedEntity.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds;

/**
 * interface to characteristics common to several components of a feed
 * TODO extend to more components
 */
public interface FeedEntity {

    public void setUrl(String url);

    public String getUrl();

    public void setTitle(String title);

    public String getTitle();

    public void setDescription(String description);

    public String getDescription();

    public void setLink(String link);

    public String getLink();

    public void setDate(String date);

    public String getDate();
}
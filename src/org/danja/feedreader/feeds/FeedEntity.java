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

public interface FeedEntity {

    public void setURIString(String uri);

    public String getURIString();

    public void setTitle(String title);

    public String getTitle();

    public void setDescription(String description);

    public String getDescription();

    public void setLink(String link);

    public String getLink();

    public void setDate(String date);

    public String getDate();
}
/*
 * Danny Ayers Aug 27, 2004 http://dannyayers.com
 *  
 */
package org.danja.feedreader.feeds;

public interface Entry extends FeedEntity {

    public void setContent(String content);

    public String getContent();

    // added for Poller
    public void setSourceTitle(String feedTitle);

    public String getSourceTitle();

    public void setSourceLink(String feedLink);

    public String getSourceLink();

    public void setAuthor(String author);
    
    public String getAuthor();
}
/**
 * feedreader-prototype
 *
 * FeedParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.InputStream;

import org.danja.feedreader.feeds.Feed;
import org.xml.sax.ContentHandler;

/**
 * common interface for feed parsers
 */
public interface FeedParser {
    public void setContentHandler(FeedHandler contentHandler);

    public void parse(InputStream inputStream);
    
    public void parse();
    
    public void setFeed(Feed feed);
    
    public Feed getFeed();

	public ContentHandler getContentHandler();
}
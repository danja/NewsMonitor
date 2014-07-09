/**
 * feedreader-prototype
 *
 * FeedParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.newsmonitor.interpreters;

import java.io.InputStream;

import org.danja.newsmonitor.model.Feed;
import org.xml.sax.ContentHandler;

/**
 * common interface for feed parsers
 */
public interface FeedParser {
	
    public void setHandler(FeedHandler handler);

    public void parse(InputStream inputStream);
    
    public void parse();
    
    public void setFeed(Feed feed);
    
    public Feed getFeed();

	public FeedHandler getHandler();
}
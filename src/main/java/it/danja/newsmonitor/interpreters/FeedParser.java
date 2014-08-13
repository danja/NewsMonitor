/**
 * NewsMonitor
 *
 * FeedParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Feed;

import java.io.InputStream;

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
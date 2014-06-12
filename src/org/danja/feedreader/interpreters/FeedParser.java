/**
 * feedreader-prototype
 *
 * FeedParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.io.InputStream;

import org.danja.feedreader.model.Feed;
import org.xml.sax.ContentHandler;

/**
 * common interface for feed parsers
 */
public interface FeedParser {
	
    public void setHandler(FeedHandlerBase contentHandler);

    public void parse(InputStream inputStream);
    
    public void parse();
    
    public void setFeed(Feed feed);
    
    public Feed getFeed();

	public FeedHandlerBase getHandler();
}
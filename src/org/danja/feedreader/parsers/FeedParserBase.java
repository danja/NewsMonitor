/**
 * feedreader-prototype
 *
 * FeedParserBase.java
 * @author danja
 * @date Apr 28, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.InputStream;

import org.danja.feedreader.feeds.Feed;
import org.xml.sax.ContentHandler;

/**
 *
 */
public abstract class FeedParserBase implements FeedParser {

	 FeedHandler contentHandler;
	 
	private Feed feed;
	// private Feed feed;

	/* (non-Javadoc)
	 * @see org.danja.feedreader.parsers.FeedParser#setContentHandler(org.xml.sax.ContentHandler)
	 */
//	@Override
//	public void setContentHandler(FeedHandler contentHandler) {
//this.contentHandler = contentHandler;
//contentHandler.setFeed(feed);
//	}
	
	public FeedHandler getContentHandler() {
		return contentHandler;
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.parsers.FeedParser#setFeed(org.danja.feedreader.feeds.Feed)
	 */
	@Override
	public void setFeed(Feed feed) {
		this.feed = feed;
		
	}
	
	public void parse() {
		System.out.println("parse() called");
		parse(feed.getInputStream());
	}


	@Override
	public Feed getFeed() {
		return feed;
	}
}

/**
 * feedreader-prototype
 *
 * FeedParserBase.java
 * @author danja
 * @date Apr 28, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.io.IOException;
import java.io.InputStream;

import org.danja.feedreader.model.Feed;
import org.xml.sax.ContentHandler;

/**
 *
 */
public abstract class FeedParserBase implements FeedParser {

	private FeedHandlerBase feedHandler;

	private Feed feed;

	// private Feed feed;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.danja.feedreader.parsers.FeedParser#setContentHandler(org.xml.sax
	 * .ContentHandler)
	 */
	@Override
	public void setHandler(FeedHandlerBase contentHandler) {
		this.feedHandler = contentHandler;
	}

	public FeedHandlerBase getHandler() {
		return feedHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.danja.feedreader.parsers.FeedParser#setFeed(org.danja.feedreader.
	 * feeds.Feed)
	 */
	@Override
	public void setFeed(Feed feed) {
		this.feed = feed;
		feedHandler.setFeed(feed);
	}

	public void parse() {
		// System.out.println("parse() called");
		InputStream stream = feed.getInputStream();
		parse(stream);
	}

	@Override
	public Feed getFeed() {
		return feed;
	}
}

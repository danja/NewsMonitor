/**
 * NewsMonitor
 *
 * FeedParserBase.java
 * @author danja
 * @date Apr 28, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Feed;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.ContentHandler;

/**
 *
 */
public abstract class FeedParserBase implements FeedParser {

	private FeedHandler feedHandler;

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
	public void setHandler(FeedHandler contentHandler) {
		this.feedHandler = contentHandler;
	}

	public FeedHandler getHandler() {
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

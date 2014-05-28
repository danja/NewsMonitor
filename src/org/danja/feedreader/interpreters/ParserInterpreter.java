/**
 * feedreader-prototype
 *
 * ParserInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.parsers.FeedParser;
import org.xml.sax.ContentHandler;

/**
 *  
 */
public class ParserInterpreter extends InterpreterBase {

	private FeedParser feedParser;

//	private ContentHandler contentHandler;

	public ParserInterpreter(Feed feed, FeedParser feedParser) {
		super();
		this.feedParser = feedParser;
		feedParser.setFeed(feed);
	}

	public void interpret(Feed feed) {
		System.out.println("interpret(Feed feed), feed = "+feed.getUrl());
		// feedParser.setFeed(feed);
		feedParser.parse();
	}

	public String toString() {
		ContentHandler contentHandler = feedParser.getHandler(); 
		StringBuffer buffer = new StringBuffer();
		buffer.append("Interpreter : " + getClass().getSimpleName() + "\n");
		buffer.append("feedParser = ");
		if (feedParser != null) {
			buffer.append(feedParser.getClass().getSimpleName() + "\n");
		} else {
			buffer.append("null\n");
		}
		buffer.append("handler = ");
		if (contentHandler != null) {
			buffer.append(contentHandler.getClass().getSimpleName() + "\n");
		} else {
			buffer.append("null\n");
		}
		return buffer.toString();
	}
}
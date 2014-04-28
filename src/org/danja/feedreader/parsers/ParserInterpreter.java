/**
 * feedreader-prototype
 *
 * ParserInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterBase;
import org.xml.sax.ContentHandler;

/**
 *  
 */
public class ParserInterpreter extends InterpreterBase {

	private FeedParser feedParser;

//	private ContentHandler contentHandler;

	public ParserInterpreter(FeedParser feedParser) {
		super();
		this.feedParser = feedParser;
	}

//	public void setContentHandler(FeedHandler contentHandler) {
//		feedParser.setContentHandler(contentHandler);
//	}

	public void interpret(Feed feed) {
		System.out.println("interpret(Feed feed), feed = "+feed.getUrl());
		feedParser.setFeed(feed);
		System.out.println("getContentHandler() = "+getContentHandler());
		feedParser.setContentHandler(getContentHandler());
		// feedParser.getContentHandler(contentHandler);
		System.out.println("feedParser.getContentHandler() = "+feedParser.getContentHandler());
		feedParser.parse();
	System.out.println(feedParser.getClass().getSimpleName());
		// feedParser.parse(feed.getInputStream());
	}

	public String toString() {
		ContentHandler contentHandler = feedParser.getContentHandler(); 
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
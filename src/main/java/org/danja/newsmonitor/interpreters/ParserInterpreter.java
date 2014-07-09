/**
 * feedreader-prototype
 *
 * ParserInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.newsmonitor.interpreters;

import org.danja.newsmonitor.model.Feed;
import org.danja.newsmonitor.model.Page;
import org.xml.sax.ContentHandler;

/**
 *  
 */
public class ParserInterpreter extends InterpreterBase implements Interpreter {

	private FeedParser feedParser;

	public ParserInterpreter(Feed feed, FeedParser feedParser) {
		super();
		this.feedParser = feedParser;
		feedParser.setFeed(feed);
	}

	public void interpret(Page page) {  
		feedParser.parse();
	}

	public String toString() {
		ContentHandler contentHandler = feedParser.getHandler(); 
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getSimpleName() + "\n");
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
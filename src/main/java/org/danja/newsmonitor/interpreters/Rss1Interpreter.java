/**
 * feedreader-prototype
 *
 * Rss1Interpreter.java
 * @author danja
 * @date Jun 10, 2014
 *
 */
package org.danja.newsmonitor.interpreters;

import org.danja.newsmonitor.model.Feed;

/**
 * TODO push RSS 1.0 to server
 */
public class Rss1Interpreter extends ParserInterpreter {

	/**
	 * @param feed
	 * @param feedParser
	 */
	public Rss1Interpreter(Feed feed, FeedParser feedParser) {
		super(feed, feedParser);
	}

	public void interpret(Feed feed) {
		super.interpret(feed);
		System.out.println("RSS 1.0 Interpreter");
	}
}

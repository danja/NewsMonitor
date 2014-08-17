/**
 * NewsMonitor
 *
 * Rss1Interpreter.java
 * @author danja
 * @date Jun 10, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.model.Feed;

/**
 * TODO push RSS 1.0 to server
 */
public class Rss1Interpreter extends ParserInterpreter {

	private static Logger log = LoggerFactory.getLogger(ParserInterpreter.class);
	
	/**
	 * @param feed
	 * @param feedParser
	 */
	public Rss1Interpreter(Feed feed, FeedParser feedParser) {
		super(feed, feedParser);
	}

	public void interpret(Feed feed) {
		super.interpret(feed);
		log.info("RSS 1.0 Interpreter");
	}
}

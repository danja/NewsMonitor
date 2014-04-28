/**
 * TODO rename?
 * feedreader-prototype
 *
 * Interpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.parsers.AtomHandler;
import org.danja.feedreader.parsers.FeedHandler;

/**
 * Bridges between feed and parser
 *  
 */
public interface Interpreter  { 

    public void interpret(Feed feed);

	public void setContentHandler(FeedHandler feedHandler);
	
	public FeedHandler getContentHandler();
}
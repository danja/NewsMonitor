/**
 * feedreader-prototype
 *
 * Interpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.Page;

/**
 * Bridges between feed and parser
 *  
 *  @see FeedParser
 *  @see Feed
 */
public interface Interpreter  { 

    public void interpret(Page page);
}
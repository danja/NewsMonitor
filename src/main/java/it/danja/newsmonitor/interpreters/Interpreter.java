/**
 * NewsMonitor
 *
 * Interpreter.java
 * 
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.Page;

/**
 * Bridges between feed and parser
 *  
 *  @see FeedParser
 *  @see Feed
 */
public interface Interpreter  { 

    public void interpret(Page page);
}
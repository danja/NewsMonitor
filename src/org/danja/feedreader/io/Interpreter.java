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
package org.danja.feedreader.io;

import org.danja.feedreader.feeds.Feed;

/**
 * Bridges between feed and parser etc
 *  
 */
public interface Interpreter  { 

    public void interpret(Feed feed);
}
package org.danja.feedreader.io;

import org.danja.feedreader.feeds.FeedFetcher;

/**
 * Bridges between feed and parser etc
 * 
 * @version $Revision$
 *  
 */
public interface Interpreter  { // CHANGED!! was extends ContentHandler

    public void interpret(FeedFetcher feed);
}
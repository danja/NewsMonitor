/*
 * Danny Ayers Oct 31, 2004
 * http://dannyayers.com
 * 
 */
package org.danja.feedreader.social;

import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.io.Interpreter;


public class WriterInterpreter implements Interpreter {

    public void interpret(FeedFetcher feed) {
        System.out.println("\nFeed: "+feed.getURIString());
        System.out.println("type: "+ FeedConstants.formatName(feed.getFormatHint()));
        System.out.println("Writing from RDFInterpreter...");
        feed.downloadToFile("data/"+RDFInterpreterFactory.getFilename(feed));
    }

}

/*
 * Danny Ayers Oct 31, 2004
 * http://dannyayers.com
 * 
 */
package org.urss.social;

import org.urss.feeds.FeedConstants;
import org.urss.feeds.FeedFetcher;
import org.urss.io.Interpreter;


public class WriterInterpreter implements Interpreter {

    public void interpret(FeedFetcher feed) {
        System.out.println("\nFeed: "+feed.getURIString());
        System.out.println("type: "+ FeedConstants.formatName(feed.getFormatHint()));
        System.out.println("Writing from RDFInterpreter...");
        feed.downloadToFile("data/"+RDFInterpreterFactory.getFilename(feed));
    }

}

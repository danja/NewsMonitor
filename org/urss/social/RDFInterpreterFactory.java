package org.urss.social;

import org.urss.feeds.EntryList;
import org.urss.feeds.FeedConstants;
import org.urss.feeds.FeedFetcher;
import org.urss.io.Interpreter;

/**
 * Looks after the creation of interpreters (gluing parsers together)
 * 
 * @version $Revision$
 *  
 */
public class RDFInterpreterFactory {

    public static Interpreter createInterpreter(char formatHint,
            EntryList entries) {
        Interpreter interpreter = null;

        switch (formatHint) {

        case FeedConstants.RSS2_BOZO:
            System.out.println("Creating BOZO interpreter...");
        interpreter = new CleanerInterpreter();
            return interpreter;

        case FeedConstants.RSS2:
        case FeedConstants.ATOM:
            System.out.println("Creating XML interpreter...");
          interpreter = new TransformerInterpreter();
            return interpreter;
 
        case FeedConstants.RSS1:
        case FeedConstants.RDF_OTHER:  
            System.out.println("Creating RDF interpreter...");
        interpreter = new WriterInterpreter();
            return interpreter;
            
        default:
            return null;
        }
    }

    public static String getFilename(FeedFetcher feed) {
        String feedFilename = feed.getURIString();
        feedFilename = feedFilename.substring(7);
       return feedFilename.replace('/','_');
    }
}
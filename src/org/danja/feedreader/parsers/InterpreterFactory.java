/**
 * feedreader-prototype
 *
 * RDFInterpreterFactory.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.io.Interpreter;

/**
 * Looks after the creation of interpreters (gluing parsers together)
 * 
 * @version $Revision$
 *  
 */
public class InterpreterFactory {

    public static Interpreter createInterpreter(char formatHint,
            EntryList entries) {
        Interpreter interpreter = null;
        FeedParser feedParser = null;
        Rss2Handler rss2handler = null;

        switch (formatHint) {
        case FeedConstants.RSS2:
            System.out.println("RSS2: Using Rss2Handler, XMLReaderParser");
          

            feedParser = new XMLReaderParser();
            interpreter = new ParserInterpreter(feedParser);
            rss2handler = new Rss2Handler();
            rss2handler.setEntryList(entries);
            feedParser.setContentHandler(rss2handler);
            return interpreter;

        case FeedConstants.RSS2_BOZO:
            System.out.println("RSS2_BOZO: Using Rss2Handler, SoupParser");
            feedParser = new SoupParser();
            interpreter = new ParserInterpreter(feedParser);
            rss2handler = new Rss2Handler();
            rss2handler.setEntryList(entries);
           
            feedParser.setContentHandler(rss2handler);
            return interpreter;

        default:
            return null;
        }

    }
}
/**
 * feedreader-prototype
 *
 * RDFInterpreterFactory.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.parsers.AtomHandler;
import org.danja.feedreader.parsers.FeedParser;
import org.danja.feedreader.parsers.Rss2Handler;
import org.danja.feedreader.parsers.SoupParser;
import org.danja.feedreader.parsers.XMLReaderParser;

/**
 * Looks after the creation of interpreters (gluing parsers together)
 * 
 * @version $Revision$
 *  
 */
public class InterpreterFactory {

    private static AtomHandler atomHandler;

	public static Interpreter createInterpreter(char formatHint) {
        Interpreter interpreter = null;
        FeedParser feedParser = null;
        Rss2Handler rss2handler = null;

// UNKNOWN, RSS1, RSS2, ATOM, RSS2_BOZO, RDF_OTHER
        
        switch (formatHint) {
        case FeedConstants.RSS1:
        	
        case FeedConstants.ATOM:
            System.out.println("Atom: Using AtomHandler, XMLReaderParser");
            feedParser = new XMLReaderParser();
            atomHandler = new AtomHandler();
            feedParser.setContentHandler(atomHandler);
            interpreter = new ParserInterpreter(feedParser);
           
          //  atomHandler.setEntryList(entries);
         //   feedParser.setContentHandler(atomHandler);
         //   interpreter.setContentHandler(atomHandler);
            
            return interpreter;
            
        case FeedConstants.RSS2:
            System.out.println("RSS2: Using Rss2Handler, XMLReaderParser");
            feedParser = new XMLReaderParser();
            interpreter = new ParserInterpreter(feedParser);
            rss2handler = new Rss2Handler();
    //        rss2handler.setEntryList(entries);
            feedParser.setContentHandler(rss2handler);
            return interpreter;

        case FeedConstants.RDF_OTHER:

        case FeedConstants.RSS2_BOZO:
        case FeedConstants.UNKNOWN:
            System.out.println("RSS2_BOZO: Using Rss2Handler, SoupHandler");
            feedParser = new SoupParser();
            interpreter = new ParserInterpreter(feedParser);
            rss2handler = new Rss2Handler();
        //    rss2handler.setEntryList(entries);
            feedParser.setContentHandler(rss2handler);
            return interpreter;

        default:
            return null;
        }

    }
}
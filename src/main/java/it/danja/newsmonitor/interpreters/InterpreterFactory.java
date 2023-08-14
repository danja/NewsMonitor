/**
 * NewsMonitor
 *
 * RDFInterpreterFactory.java
 *
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.utils.ContentType;

/**
 * Looks after the creation of interpreters (gluing parsers together)
 *
 * @version $Revision$
 *
 */
public class InterpreterFactory {

    private static Logger log = LoggerFactory.getLogger(InterpreterFactory.class);

    private static AtomHandler atomHandler;

    public static Interpreter createInterpreter(Feed feed) {
        char formatHint = feed.getFormatHint();
        Interpreter interpreter = null;
        FeedParser feedParser = null;
        // Rss2Handler rss2handler = null;

        // UNKNOWN, RSS1, RSS2, ATOM, RSS_SOUP, RDF_OTHER
        switch (formatHint) {
            case ContentType.RSS1:
                log.info("RSS1: Using Rss1Interpreter, Rss1Handler, XMLReaderParser");
                feedParser = new XMLReaderParser();
                FeedHandlerBase rss1handler = new Rss1Handler();
                feedParser.setHandler(rss1handler);
                interpreter = new ParserInterpreter(feed, feedParser);
                return interpreter;

            case ContentType.ATOM:
                log.info("Atom: Using AtomHandler, XMLReaderParser");
                feedParser = new XMLReaderParser();
                atomHandler = new AtomHandler();
                feedParser.setHandler(atomHandler);
                interpreter = new ParserInterpreter(feed, feedParser);

                // atomHandler.setEntryList(entries);
                // feedParser.setContentHandler(atomHandler);
                // interpreter.setContentHandler(atomHandler);
                return interpreter;

            case ContentType.RSS2:
                log.info("RSS2: Using Rss2Handler, XMLReaderParser");
                feedParser = new XMLReaderParser();
                FeedHandlerBase rss2handler = new Rss2Handler();
                feedParser.setHandler(rss2handler);
                interpreter = new ParserInterpreter(feed, feedParser);
                return interpreter;

                /*
            case ContentType.HTML:
                log.info("HTML: Using SoupParser, HTMLHandler");
                feedParser = new SoupParser();
                FeedHandlerBase htmlHandler = new HtmlHandler();
                feedParser.setHandler(htmlHandler);
                interpreter = new ParserInterpreter(feed, feedParser);
                return interpreter;

          */
            default:

                /*
                log.info("unknown: trying SoupParser, Rss2Handler");
                feedParser = new SoupParser();
                FeedHandlerBase handler = new Rss2Handler();
                feedParser.setHandler(handler);
                interpreter = new ParserInterpreter(feed, feedParser);
                return interpreter;
*/
                                log.info("unknown format: trying GenericParser, GenericHandler");
                feedParser = new GenericParser();
                FeedHandlerBase handler = new GenericHandler();
                feedParser.setHandler(handler);
                interpreter = new ParserInterpreter(feed, feedParser);
                return interpreter;
        }

    }
}

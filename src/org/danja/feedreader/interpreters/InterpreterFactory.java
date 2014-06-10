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

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedConstants;

/**
 * Looks after the creation of interpreters (gluing parsers together)
 * 
 * @version $Revision$
 * 
 */
public class InterpreterFactory {

	private static AtomHandler atomHandler;

	public static Interpreter createInterpreter(Feed feed) {
		char formatHint = feed.getFormatHint();
		Interpreter interpreter = null;
		FeedParser feedParser = null;
		// Rss2Handler rss2handler = null;

		// UNKNOWN, RSS1, RSS2, ATOM, RSS_SOUP, RDF_OTHER

		switch (formatHint) {
		case FeedConstants.RSS1:
			System.out.println("RSS1: Using Rss1Interpreter, Rss1Handler, XMLReaderParser");
			feedParser = new XMLReaderParser();
			FeedHandlerBase rss1handler = new Rss1Handler();
			feedParser.setHandler(rss1handler);
			interpreter = new Rss1Interpreter(feed, feedParser);
			return interpreter;

		case FeedConstants.ATOM:
			System.out.println("Atom: Using AtomHandler, XMLReaderParser");
			feedParser = new XMLReaderParser();
			atomHandler = new AtomHandler();
			feedParser.setHandler(atomHandler);
			interpreter = new ParserInterpreter(feed, feedParser);

			// atomHandler.setEntryList(entries);
			// feedParser.setContentHandler(atomHandler);
			// interpreter.setContentHandler(atomHandler);

			return interpreter;

		case FeedConstants.RSS2:
			System.out.println("RSS2: Using Rss2Handler, XMLReaderParser");
			feedParser = new XMLReaderParser();
			FeedHandlerBase rss2handler = new Rss2Handler();
			feedParser.setHandler(rss2handler);
			interpreter = new ParserInterpreter(feed, feedParser);
			return interpreter;

		case FeedConstants.RDF_OTHER:

		case FeedConstants.RSS_SOUP:
		case FeedConstants.UNKNOWN:
			System.out.println("RSS_SOUP: Using SoupParser, SoupHandler");
			feedParser = new SoupParser();
			FeedHandlerBase handler = new Rss2Handler();
			feedParser.setHandler(handler);
			interpreter = new ParserInterpreter(feed, feedParser);
			return interpreter;

		default:
			return null;
		}

	}
}
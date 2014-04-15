/*
 * Danny Ayers Aug 31, 2004
 * http://dannyayers.com
 * 
 */
package org.urss.parsers;

import org.urss.feeds.FeedFetcher;
import org.urss.io.Interpreter;
import org.xml.sax.ContentHandler;

/**
 * @author danny
 *  
 */
public class ParserInterpreter  implements Interpreter { 

    private FeedParser feedParser;

    private ContentHandler contentHandler;

    public ParserInterpreter(FeedParser feedParser) {
        super();
        this.feedParser = feedParser;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        feedParser.setContentHandler(contentHandler);
    }

    public void interpret(FeedFetcher feed) {
        feedParser.parse(feed.getInputStream());
    }
}
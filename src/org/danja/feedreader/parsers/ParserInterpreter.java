/*
 * Danny Ayers Aug 31, 2004
 * http://dannyayers.com
 * 
 */
package org.danja.feedreader.parsers;

import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.io.Interpreter;
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
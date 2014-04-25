/**
 * feedreader-prototype
 *
 * ParserInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.io.Interpreter;
import org.xml.sax.ContentHandler;

/**
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
/**
 * feedreader-prototype
 *
 * FeedParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.InputStream;

import org.xml.sax.ContentHandler;

/**
 * common interface for feed parsers
 */
public interface FeedParser {
    public void setContentHandler(ContentHandler contentHandler);

    public void parse(InputStream inputStream);
}
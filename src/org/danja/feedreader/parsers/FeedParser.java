package org.danja.feedreader.parsers;

import java.io.InputStream;

import org.xml.sax.ContentHandler;

/**
 * wrapper for parsers
 * 
 * @version $Revision$
 */
public interface FeedParser {
    public void setContentHandler(ContentHandler contentHandler);

    public void parse(InputStream inputStream);

   // added for Poller
  //  public ContentHandler getContentHandler();
}
/**
 * feedreader-prototype
 *
 * XMLReaderParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.feeds.Feed;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Wrapper around SAX2 Parser
 *  
 */
public class XMLReaderParser implements FeedParser {

    private XMLReader reader = null;

    private Feed feed;

    private boolean unescape;

    public XMLReaderParser() {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            reader = parserFactory.newSAXParser().getXMLReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentHandler(ContentHandler contentHandler) {
        reader.setContentHandler(contentHandler);
    }

    public void parse(InputStream inputStream) {
        InputSource inputSource = new InputSource(inputStream);
        try {
            reader.parse(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    // added for Poller
   // public ContentHandler getContentHandler() {
     //   return reader. getContentHandler();
 //   }
}
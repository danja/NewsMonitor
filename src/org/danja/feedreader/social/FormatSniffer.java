/**
 * feedreader-prototype
 *
 * FormatSniffer.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.social;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.feeds.FeedConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class FormatSniffer extends DefaultHandler{

    char format = FeedConstants.UNKNOWN;
    
    public char sniff(InputStream inputStream) {
    
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        XMLReader reader = null;
        try {
            reader = parserFactory.newSAXParser().getXMLReader();
        } catch (Exception e) { // bad
            e.printStackTrace();
        }
        reader.setContentHandler(this);
        InputSource inputSource = new InputSource(inputStream);
        try {
            reader.parse(inputSource); // TODO TIMEOUT HERE!?
        } catch (Exception e) { // whatever the problem, there's no XML available
            return FeedConstants.RSS2_BOZO;
        }
        return format; // 
    }
    
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        if(namespaceURI.equals(FeedConstants.RDF_NS) && localName.equals("RDF")){
            format = FeedConstants.RDF_OTHER;
        }
        if(namespaceURI.equals(FeedConstants.RSS_NS) && localName.equals("items")){
            format = FeedConstants.RSS1;
        }
        if(localName.equals("rss")){
            format = FeedConstants.RSS2;
        }
        if(namespaceURI.startsWith("http://purl.org/atom") && localName.equals("feed")){
            format = FeedConstants.ATOM;
        }
    }
}
/**
 * feedreader-prototype
 *
 * RSS2Handler.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import org.danja.feedreader.feeds.Entry;
import org.danja.feedreader.feeds.EntryImpl;
import org.danja.feedreader.feeds.EntryList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for RSS 2.0 XML
 *  
 */

public class Rss2Handler extends FeedHandler {

    private String sourceURI = "";

    private String sourceTitle = "";
    
    private String author = "";
    
    private final static char IN_NOTHING = 0;

    private final static char IN_CHANNEL = 1;

    private final static char IN_ITEM = 2;

    private char state = IN_NOTHING;

    private StringBuffer textBuffer;

    private Entry entry;

    private EntryList entries;

    // added for Poller
    private String feedTitle = "";
    private String feedLink = "";
    
    public Rss2Handler() {
        textBuffer = new StringBuffer();
    }

    public void setEntryList(EntryList entries) {
        this.entries = entries;
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        switch (state) {
        case IN_NOTHING:
            if ("channel".equals(localName)) {
                state = IN_CHANNEL;
            }
            return;

        case IN_CHANNEL:
//          added for Poller
            textBuffer = new StringBuffer();
            if ("item".equals(localName)) { // TODO is this doing anything?
                state = IN_ITEM;
                entry = new EntryImpl();
//              added for Poller
             
                entry.setAuthor(author);
                entry.setSourceTitle(feedTitle); 
                entry.setSourceLink(feedLink); 
            }
            return;

        case IN_ITEM:
            textBuffer = new StringBuffer();
            return;

        default:
            return;
        }
    }

    public void characters(char[] ch, int start, int length) {
        textBuffer.append(ch, start, length);
    }

    public void endElement(String namespaceURI, String localName, String qName) {

        switch (state) {

        case IN_NOTHING:
            return;

        case IN_CHANNEL: // switch down
            if ("channel".equals(localName)) {
                state = IN_NOTHING;
            }
            
            // added for Poller
            if ("title".equals(localName)) {
                feedTitle = textBuffer.toString();
                return;
            }
            if ("author".equals(localName)) {
                author = textBuffer.toString();
                return;
            }
            if ("link".equals(localName)) {
                feedLink = textBuffer.toString();
                return;
            }
            return;

        case IN_ITEM:
            if ("item".equals(localName)) {
                state = IN_CHANNEL;
                entries.addEntry(entry);
                return;
            }
            if ("title".equals(localName)) {
                entry.setTitle(textBuffer.toString());
                return;
            }
            if ("description".equals(localName)) {
            	System.out.println("inRSS2 parser textBuffer.toString() = "+textBuffer.toString());
                entry.setContent(textBuffer.toString());
                return;
            }

            // added for Poller
            if ("guid".equals(localName)) {
                entry.setUrl(textBuffer.toString());
                return;
            }
            if ("pubDate".equals(localName)) {
                entry.setDate(textBuffer.toString());
                return;
            }

            if ("link".equals(localName)) {
                entry.setLink(textBuffer.toString());
                return;
            }

            textBuffer.append(localName);
            return;

        default:
            return;
        }
    }

    public void endDocument() throws SAXException {
    	System.out.println("Rss2Handler entry = "+entry);
    }
    


}


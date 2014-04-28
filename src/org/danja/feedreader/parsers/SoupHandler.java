/**
 * feedreader-prototype
 *
 * SoupHandler.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.danja.feedreader.feeds.Feed;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * 
 * Tag soup/ill-formed HTML/XML parser
 * 
 * produces SAX2 callbacks mostly namespace-aware (can't handle nested default
 * namespaces)
 * 
 * fairly direct port from Python version:
 * http://dannyayers.com/archives/2004/07/11/tag-soup-play-code/
 *  
 */
public class SoupHandler extends FeedHandler { // was extends FeedParserBase implements ContentHandler

    private String name;

    public SoupHandler() {
       // this.defaultNS = "";
      //  setContentHandler(this);
    }

    public void startDocument() throws SAXException {
        System.out.println("Start Document");
    }

    public void endDocument() throws SAXException {
        System.out.println("End Document");
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        System.out.println("\nstartElement- \nns:" + namespaceURI + "\nname:"
                + localName + "\nqname:" + qName + "\natts:");
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.println("   " + atts.getLocalName(i) + " "
                    + atts.getValue(i));
        }

    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        System.out.println("\nendElement- \nns:" + namespaceURI + "\nname:"
                + localName + "\nqname:" + qName);

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        StringBuffer buffer = new StringBuffer();

        buffer.append(ch, start, length);
        System.out.println("characters: " + buffer);
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        System.out.println("PI target = '" + target + "'");
        System.out.println("data = '" + data + "'");
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        // not implemented
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        // not implemented
    }

    public void skippedEntity(String name) throws SAXException {
        // not implemented
    }

    public void setDocumentLocator(Locator locator) {
        // not implemented
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // not implemented
    }

    public void parse(InputStream inputStream) {
        StringBuffer buffer = new StringBuffer();
        int character;
        try {
            while ((character = inputStream.read()) != -1) {
                buffer.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
   //     parseData(buffer.toString());
    }


    public static void main(String[] args) {
        File inputFile = new File(args[0]);
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream in = new FileInputStream(inputFile);
            SoupHandler parser = new SoupHandler();
            parser.parse(in);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
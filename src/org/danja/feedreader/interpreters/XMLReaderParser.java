/**
 * feedreader-prototype
 *
 * XMLReaderParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.model.Feed;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Wrapper around SAX2 Parser
 *  
 */
public class XMLReaderParser extends FeedParserBase implements FeedParser {

    private XMLReader reader = null;

    //private Feed feed;

    private boolean unescape;

    public XMLReaderParser() {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            reader = parserFactory.newSAXParser().getXMLReader();
            
            // just used to check te parser was actually being called
        //   reader.setFeature("http://xml.org/sax/features/validation", true);
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHandler(FeedHandler contentHandler) {
    	super.setHandler(contentHandler); // is ok?
        reader.setContentHandler(contentHandler);
    }

    public void parse(InputStream inputStream) {
    //	System.out.println("parse() called");
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setEncoding("UTF-8"); // ?? isneeded
        try {
            reader.parse(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        
        try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    //    System.out.println("after parse");
    }
}
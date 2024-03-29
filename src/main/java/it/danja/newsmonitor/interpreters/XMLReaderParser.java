/**
 * NewsMonitor
 *
 * XMLReaderParser.java
 * 
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.model.Feed;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Wrapper around SAX2 Parser
 *  
 */
public class XMLReaderParser extends FeedParserBase implements FeedParser {

	private static Logger log = LoggerFactory.getLogger(FeedParserBase.class);
	
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
            log.error(e.getMessage());
        }
    }

    public void setHandler(FeedHandler contentHandler) {
    	super.setHandler(contentHandler); // is ok?
        reader.setContentHandler(contentHandler);
    }

    public void parse(InputStream inputStream) {
    //	log.info("parse() called");
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setEncoding("UTF-8"); // ?? isneeded
        try {
            reader.parse(inputSource);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SAXException e) {
            log.error(e.getMessage());
        }
        
        try {
			inputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		} 
    //    log.info("after parse");
    }
}
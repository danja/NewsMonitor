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

    public void setHandler(FeedHandlerBase contentHandler) {
    	super.setHandler(contentHandler); // is ok?
        reader.setContentHandler(contentHandler);
    }

    public void parse(InputStream inputStream) {

    	 InputSource inputSource = new InputSource(inputStream);
    	
//    	XmlEncodingSniffer sniffer = null;
//    	InputStream correctedInputStream = null;
//      try {
//		sniffer = new XmlEncodingSniffer(inputStream, "UTF-8");
//		correctedInputStream = sniffer.getStream();
//	} catch (UnsupportedEncodingException e1) {
//		e1.printStackTrace();
//	} catch (IOException e1) {
//		e1.printStackTrace();
//	}
//      InputSource inputSource = new InputSource(correctedInputStream);
    	 
    	 
    	// desperately trying for UTF-8
    //    Reader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
   //   Reader reader = sniffer.getReader();
      
    //    inputSource.setCharacterStream(reader);
    //    inputSource.setEncoding("UTF-8"); 
        
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
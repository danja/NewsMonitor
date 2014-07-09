package org.danja.newsmonitor.interpreters;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlLint {
    public static void main(String[] args) {
        try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            
            XMLReader r = factory.newSAXParser().getXMLReader();
            r.setErrorHandler(new DraconianErrorHandler());
            r.setContentHandler(new DefaultHandler());
            r.parse(new InputSource(System.in));
            System.exit(0);
        } catch( Exception e ) {
            System.exit(-1);
        }
    }
    
    private static class DraconianErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            throw e;
        }
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }
        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }
    }
}
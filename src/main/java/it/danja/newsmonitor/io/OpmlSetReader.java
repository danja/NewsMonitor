/**
 * TODO reintegrate
 * feedreader-prototype
 *
 * OpmlSetReader.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class OpmlSetReader extends DefaultHandler {

    private Set feedURIs;

    public Set load(String storeFilename) {
        feedURIs = new HashSet();
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            XMLReader xmlReader = parserFactory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(this);
            InputStream inputStream = new FileInputStream(new File(
                    storeFilename));
            InputSource inputSource = new InputSource(inputStream);
            xmlReader.parse(inputSource);
            /*
             * Xerces version XMLReader reader = XMLReaderFactory
             * .createXMLReader(ContentType.DEFAULT_PARSER_NAME);
             * reader.setContentHandler(this); InputStream inputStream = new
             * FileInputStream(new File( storeFilename)); InputSource
             * inputSource = new InputSource(inputStream);
             * reader.parse(inputSource);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedURIs;
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        
    /*
        System.out.println("\nnamespaceURI:" + namespaceURI);
        System.out.println("localName:" + localName);
        System.out.println("qName:" + qName);
        for(int i=0;i<attributes.getLength();i++){
           System.out.println("   attribute "+attributes.getLocalName(i)+" = "+attributes.getValue(i));
        }
        */
        
        if (localName.equals("outline")) {
            String uriString = attributes.getValue("xmlUrl");
            feedURIs.add(uriString);
        }
    }

    public static void main(String[] args) {
        OpmlSetReader reader = new OpmlSetReader();
        Set channelSet = reader.load(args[0]);
        Iterator channelIterator = channelSet.iterator();
        while (channelIterator.hasNext()) {
            System.out.println(channelIterator.next());
        }
    }

}
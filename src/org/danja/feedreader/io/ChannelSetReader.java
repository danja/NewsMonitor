/* TODO RETIRE THIS
 * Danny Ayers Aug 18, 2004 http://dannyayers.com
 *  
 */
package org.danja.feedreader.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.feeds.FeedConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ChannelSetReader extends DefaultHandler {

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
   
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedURIs;
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {

        if (namespaceURI.equals(FeedConstants.RSS_NS)
                && localName.equals("channel")) {

            String uriString = attributes.getValue(FeedConstants.RDF_NS,
                    "about");
            feedURIs.add(uriString);
        }
    }

    public static void main(String[] args){
        ChannelSetReader reader = new ChannelSetReader();
        Set channelSet = reader.load(args[0]);
        Iterator channelIterator = channelSet.iterator();
        while(channelIterator.hasNext()){
            System.out.println(channelIterator.next());
        }
    }

}
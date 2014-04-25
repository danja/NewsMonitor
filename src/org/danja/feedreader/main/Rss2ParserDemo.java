/**
 * feedreader-prototype
 *
 * Rss2ParserDemo.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.parsers.Rss2Handler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class Rss2ParserDemo {


    public EntryList readFeed(String feedFilename) {
        EntryList entries = new EntryListImpl();
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            XMLReader xmlReader = parserFactory.newSAXParser().getXMLReader();

            Rss2Handler handler = new Rss2Handler();
            handler.setEntryList(entries);

            xmlReader.setContentHandler(handler);
            InputStream inputStream = new FileInputStream(
                    new File(feedFilename));
            InputSource inputSource = new InputSource(inputStream);
            xmlReader.parse(inputSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
    }

    public static void main(String[] args) {
        Rss2ParserDemo reader = new Rss2ParserDemo();
        String url = DemoConstants.RSS2_SAMPLE_URL;
        if(args.length > 0) {
        	url = args[0];
        }
        EntryList entries = reader.readFeed(url);
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(entries.getEntry(i));
        }
    }
}
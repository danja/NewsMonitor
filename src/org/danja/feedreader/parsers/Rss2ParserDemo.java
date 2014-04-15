/*
 * Danny Ayers Aug 18, 2004 http://dannyayers.com
 *  
 */
package org.danja.feedreader.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
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
        EntryList entries = reader.readFeed(args[0]);
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(entries.getEntry(i));
        }
    }
}
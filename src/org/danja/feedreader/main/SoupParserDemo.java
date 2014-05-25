/**
 * feedreader-prototype
 *
 * SoupParserDemo.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.impl.EntryListImpl;
import org.danja.feedreader.parsers.Rss2Handler;
import org.danja.feedreader.parsers.SoupHandler;
import org.danja.feedreader.parsers.SoupParser;
import org.xml.sax.helpers.DefaultHandler;

public class SoupParserDemo extends DefaultHandler {


    public EntryList readFeed(String feedFilename) {
        EntryList entries = new EntryListImpl();
        try {
       //     SAXParserFactory parserFactory = SAXParserFactory.newInstance();
       //     parserFactory.setNamespaceAware(true);
        //    XMLReader xmlReader = parserFactory.newSAXParser().getXMLReader();
            SoupParser parser = new SoupParser(); //+
            Rss2Handler handler = new Rss2Handler();
            handler.setEntryList(entries);

            parser.setContentHandler(handler); //+
            InputStream inputStream = new FileInputStream(
                    new File(feedFilename));
          //  InputSource inputSource = new InputSource(inputStream);
         //   xmlReader.parse(inputSource); 
            parser.parse(inputStream);//+

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
    }

    public static void main(String[] args) {
        SoupParserDemo reader = new SoupParserDemo();
        EntryList entries = reader.readFeed(args[0]);
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(entries.getEntry(i));
        }
    }
}
/**
 * NewsMonitor
 *
 * SoupHandler.java
 * 
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Feed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 *  
 */
public class SoupHandler extends Rss2Handler { // was extends FeedParserBase implements ContentHandler

    private String name;

    public SoupHandler() {
       // this.defaultNS = "";
      //  setContentHandler(this);
    }





//    public static void main(String[] args) {
//        File inputFile = new File(args[0]);
//        StringBuffer buffer = new StringBuffer();
//        try {
//            FileInputStream in = new FileInputStream(inputFile);
//            SoupHandler parser = new SoupHandler();
//            parser.parse(in);
//            in.close();
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
}
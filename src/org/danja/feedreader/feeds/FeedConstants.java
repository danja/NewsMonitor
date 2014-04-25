/**
 * feedreader-prototype
 *
 * FeedConstants.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds;

/**
 * Different types of feeds for use in hints 
 *  
 */
public class FeedConstants {

   // public static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final String RSS_NS = "http://purl.org/rss/1.0/";
    
    public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    
    public static final String DC_NS = "http://purl.org/dc/elements/1.1/";

    public static final char UNKNOWN = 0;

    public static final char RSS1 = 1;

    public static final char RSS2 = 2;
    
    public static final char ATOM = 3;

    public static final char RSS2_BOZO = 4; //++
    
    public static final char RDF_OTHER = 5; 
    
    public static final String[] FORMAT_NAMES = {"Unknown", "RSS 1.0", "RSS 2.0", "Atom", 
            "Bozo", "RDF/XML (non-RSS)"};


    
    public static String formatName(char format){
        return FORMAT_NAMES[format];
    }
    
    // changed for Social 
  //  public static final char BOZO = 5;     
   // public static final char XML = 6; 
   // public static final char RDF_XML = 7; 
}
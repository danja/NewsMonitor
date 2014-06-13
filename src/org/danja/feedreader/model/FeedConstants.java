/**
 * feedreader-prototype
 *
 * FeedConstants.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.model;

/**
 *  
 *  
 */
public class FeedConstants {

    public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final String RSS1_NS = "http://purl.org/rss/1.0/";
    
    public static final String ATOM_NS = "http://www.w3.org/2005/Atom";
    
    public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    
    public static final String DC_NS = "http://purl.org/dc/elements/1.1/";

    // Different types of feeds for use in hints
    // TODO refactor to enum?
    public static final char UNKNOWN = 0;

    public static final char RSS1 = 1;

    public static final char RSS2 = 2;
    
    public static final char ATOM = 3;

    public static final char RSS_SOUP = 4; //++
    
    public static final char RDF_OTHER = 5; 
    
	public static final char HTML = 6;
    
    public static final String[] FORMAT_NAMES = {"Unknown", "RSS 1.0", "RSS 2.0", "Atom", 
            "RSS Soup", "RDF/XML (non-RSS)", "HTML"};



    
    public static String formatName(char format){
        return FORMAT_NAMES[format];
    }
    
}
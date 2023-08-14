/**
 * NewsMonitor
 *
 * ContentType.java
 * 
 * @author danja
 * dc:date Apr 25, 2014
 * 
 * see also javax.ws.rs.core.MediaType
 *
 */
package it.danja.newsmonitor.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  
 *  
 */
public class ContentType {

	// Different types of feeds for use in hints
	// loads of overlap, very fuzzy
	// TODO refactor to enum?
	public static final char UNKNOWN = 0;
	public static final char RSS1 = 1;
	public static final char RSS2 = 2;
	public static final char ATOM = 3;
	public static final char RSS_SOUP = 4; // ++
	public static final char RDF_OTHER = 5;
	public static final char HTML = 6;
	public static final char XML = 7;
	public static final char TEXT = 8;
	public static final char TURTLE = 9;
	public static final char UNAVAILABLE = 10;
	
	public static final Character UNKNOWN_CHARACTER = new Character((char) 0);
	public static final Character  RSS1_CHARACTER = new Character((char) 1);
	public static final Character  RSS2_CHARACTER = new Character((char) 2);
	public static final Character  ATOM_CHARACTER = new Character((char) 3);
	public static final Character  RSS_SOUP_CHARACTER = new Character((char) 4);
	public static final Character  RDF_OTHER_CHARACTER = new Character((char) 5);
	public static final Character  HTML_CHARACTER = new Character((char) 6);
	public static final Character  XML_CHARACTER = new Character((char) 7);
	public static final Character  TEXT_CHARACTER = new Character((char) 8);
	public static final Character  TURTLE_CHARACTER = new Character((char) 9);

	public static final String[] FORMAT_NAMES = { "Unknown", "RSS 1.0",
			"RSS 2.0", "Atom", "RSS Soup", "RDF/XML (non-RSS)", "HTML",
			"XML (other)", "Text", "Turtle","Unavailable" };
	

	public static String formatName(char format) {
		return FORMAT_NAMES[format];
	}

	// Maps don't work with primitives - must be a neater approach...
	public static Map<String, Character> TYPE_MAP = new HashMap<String, Character>();
	public static Map<String, Character> EXTENSION_MAP = new HashMap<String, Character>();
	public static Map<String, Character> KEYWORD_MAP = new HashMap<String, Character>();
	
	static {
		// TYPE_MAP.put("*", UNKNOWN);
		TYPE_MAP.put("application/rdf+xml", RDF_OTHER_CHARACTER);
		TYPE_MAP.put("application/rss+xml", RSS2_CHARACTER);
		TYPE_MAP.put("application/atom+xml", ATOM_CHARACTER);
		TYPE_MAP.put("application/xml", XML_CHARACTER);
		TYPE_MAP.put("application/xhtml+xml", HTML_CHARACTER);
		TYPE_MAP.put("text/plain", TEXT_CHARACTER);
		TYPE_MAP.put("text/xml", XML_CHARACTER);
		TYPE_MAP.put("text/html", HTML_CHARACTER);
		TYPE_MAP.put("text/turtle", TURTLE_CHARACTER);

		EXTENSION_MAP.put("rdf", RDF_OTHER_CHARACTER);
		EXTENSION_MAP.put("rss", RSS2_CHARACTER);
		EXTENSION_MAP.put("atom", ATOM_CHARACTER);
		EXTENSION_MAP.put("xml", XML_CHARACTER);
		EXTENSION_MAP.put("html", HTML_CHARACTER);
		EXTENSION_MAP.put("htm", HTML_CHARACTER);
		EXTENSION_MAP.put("xhtml", HTML_CHARACTER);
		EXTENSION_MAP.put("txt", TEXT_CHARACTER);
		EXTENSION_MAP.put("ttl", TURTLE_CHARACTER);
		EXTENSION_MAP.put("turtle", TURTLE_CHARACTER);
		
		KEYWORD_MAP.put("http://purl.org/rss/1.0/", RSS1_CHARACTER);
		KEYWORD_MAP.put("<rss", RSS2_CHARACTER);
		KEYWORD_MAP.put("<feed", ATOM_CHARACTER);
		KEYWORD_MAP.put("<html", HTML_CHARACTER);
		KEYWORD_MAP.put("<RDF:rdf", RDF_OTHER_CHARACTER); // may be overlap, but it's only a guess
	}

	public static char identifyExtension(String name) {
		String[] split = name.split("\\.");
		if (split.length < 2) {
			return 0;
		}
		String extension = split[split.length - 1].toLowerCase();
		// log.info("EXT = "+extension);
		if (EXTENSION_MAP.containsKey(extension)) {
			return EXTENSION_MAP.get(extension).charValue();
		}
		return 0;
	}

	public static char identifyFormat(String contentType, String data) {
		if(contentType != null) {
			return identifyFormat(contentType);
		}
		Iterator<String> iterator = KEYWORD_MAP.keySet().iterator();
		while(iterator.hasNext()){
			String keyword = iterator.next();
			if(data.indexOf(keyword) < 500) {
				return KEYWORD_MAP.get(keyword).charValue();
			}
		}
		return UNKNOWN;
	}
	
	public static char identifyFormat(String contentType) {
		if(contentType == null) {
			return UNKNOWN;
		}
		String[] split = contentType.split(";");
		if (split.length > 1) {
			contentType = split[0];
		}
		contentType = contentType.trim();
		
		if (TYPE_MAP.containsKey(contentType)) {
			// log.info("CONTAINS "+ContentType.formatName(TYPE_MAP.get(contentType).charValue()));
			return TYPE_MAP.get(contentType).charValue();
		}
		return 0;
	}

	public static String getTypeName(String type) {
		return formatName(identifyFormat(type));
	}
}
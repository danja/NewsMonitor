/**
 * feedreader-prototype
 *
 * ContentType.java
 * 
 * @author danja
 * @date Apr 25, 2014
 * 
 * see also javax.ws.rs.core.MediaType
 *
 */
package org.danja.feedreader.model;

import java.util.HashMap;
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

	public static final String[] FORMAT_NAMES = { "Unknown", "RSS 1.0",
			"RSS 2.0", "Atom", "RSS Soup", "RDF/XML (non-RSS)", "HTML",
			"XML (other)", "Text", "Turtle" };

	public static String formatName(char format) {
		return FORMAT_NAMES[format];
	}

	public static Map<String, Character> TYPE_MAP = new HashMap<String, Character>();
	public static Map<String, Character> EXTENSION_MAP = new HashMap<String, Character>();
	static {
		// TYPE_MAP.put("*", UNKNOWN);
		TYPE_MAP.put("application/rdf+xml", RDF_OTHER);
		TYPE_MAP.put("application/rss+xml", RSS2);
		TYPE_MAP.put("application/atom+xml", ATOM);
		TYPE_MAP.put("application/xml", XML);
		TYPE_MAP.put("application/xhtml+xml", HTML);
		TYPE_MAP.put("text/plain", TEXT);
		TYPE_MAP.put("text/xml", XML);
		TYPE_MAP.put("text/html", UNKNOWN);
		TYPE_MAP.put("text/turtle", UNKNOWN);

		EXTENSION_MAP.put("rdf", RDF_OTHER);
		EXTENSION_MAP.put("rss", RSS2);
		EXTENSION_MAP.put("atom", ATOM);
		EXTENSION_MAP.put("xml", XML);
		EXTENSION_MAP.put("html", HTML);
		EXTENSION_MAP.put("htm", HTML);
		EXTENSION_MAP.put("xhtml", HTML);
		EXTENSION_MAP.put("txt", TEXT);
		EXTENSION_MAP.put("ttl", TURTLE);
		EXTENSION_MAP.put("turtle", TURTLE);
	}

	public static char identifyExtension(String name) {
		String[] split = name.split(".");
		if (split.length < 2) {
			return 0;
		}
		String extension = split[split.length - 1];
		if (EXTENSION_MAP.containsKey(extension)) {
			return EXTENSION_MAP.get(extension);
		}
		return 0;
	}

	public static char identifyContentType(String contentType) {
		String[] split = contentType.split(";");
		if (split.length > 1) {
			contentType = split[0];
		}
		contentType = contentType.trim();

		if (TYPE_MAP.containsKey(contentType)) {
			return TYPE_MAP.get(contentType);
		}
		return 0;
	}

}
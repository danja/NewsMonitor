/**
 * feedreader-prototype
 *
 * AtomHandler.java
 * 
 * @author danja
 * @date Apr 27, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.danja.feedreader.feeds.Entry;
import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.Person;
import org.danja.feedreader.feeds.impl.EntryImpl;
import org.danja.feedreader.feeds.impl.EntryListImpl;
import org.danja.feedreader.feeds.impl.LinkImpl;
import org.danja.feedreader.feeds.impl.PersonImpl;
import org.danja.feedreader.utils.DateConverters;
import org.danja.feedreader.utils.HtmlCleaner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for RSS 1.0 XML
 * <p>
 * Populates a {@link Feed} object (with any contained items etc.). Is naive : treats RSS 1.0 as non-RDF XML, 
 * ignoring namespaces.
 * <p>
 * Elements handled : webMaster, author, creator, guid, title,
			"pubDate", "link", "description" 
 * <p>
 * HTML content has a little cleaning/normalization, and links are pulled out of there too.
 * 
 * @see XMLReaderParser
 * @see Feed
 * @see Entry
 * @see Person
 * @see DateStamp
 * @see Link
 */

public class Rss1Handler extends FeedHandler {

	private String author = "";

	// change to enum?
	private final static char IN_NOTHING = 0;
	private final static char IN_RDF = 1;
	private final static char IN_CHANNEL = 2;
	private final static char IN_ITEM = 3;

	// handy for debugging
	private final static String[] states = { "IN_NOTHING", "IN_RDF", "IN_CHANNEL",
			"IN_ITEM"};

	private char state = IN_NOTHING;

	private StringBuffer textBuffer;

	private Entry currentEntry;

	// private EntryList entries = new EntryListImpl();
	private static final String[] textElementsArray = { "webMaster", "author", "creator", "date", "title", "link", "description", "encoded" };
	private static final Set<String> textElements = new HashSet<String>();
	static {
		Collections.addAll(textElements, textElementsArray);
	}

	private Attributes attributes;

	public Rss1Handler() {
		textBuffer = new StringBuffer();
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attrs) {

		// System.out.println("start = "+localName);
		// System.out.println("startQ = "+qName);

		if (textElements.contains(localName)) {
			textBuffer = new StringBuffer();
		}

		attributes = attrs;

		switch (state) {

		case IN_NOTHING:
			if ("RDF".equals(localName)) {
				state = IN_RDF;
				return;
			}
			return;
			
		case IN_RDF:
			if ("channel".equals(localName)) {
				String about = attributes.getValue("rdf:about");
				// System.out.println("FEED ABOUT = "+about);
				getFeed().setId(about);
				
				// favour <link> for HtmlUrl, fall back on URI
				if(getFeed().getHtmlUrl() == null || "".equals(getFeed().getHtmlUrl())){
					getFeed().setHtmlUrl(about);
				}
				state = IN_CHANNEL;
				return;
			}
			if ("item".equals(localName)) {
				String about = attributes.getValue("rdf:about");
				
				currentEntry = new EntryImpl();
				currentEntry.setId(about);
				// favour <link> for HtmlUrl, fall back on URI
				if(currentEntry.getUrl() == null || "".equals(currentEntry.getUrl())){
					currentEntry.setUrl(about);
				}
				
				state = IN_ITEM;
				return;
			}
			return;
			
		case IN_CHANNEL:
			return;

		default:
			return;
		}
	}

	public void characters(char[] ch, int start, int length) {
		textBuffer.append(ch, start, length);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public void endElement(String namespaceURI, String localName, String qName) {

	//	System.out.println("END localName = " + localName);
	//	System.out.println("state = " + states[state]);

		String text = "";
		if (textElements.contains(localName)) {
			text = textBuffer.toString();
			if (text.length() > 0) {
				text = text.trim();
			}
		}

		switch (state) {

		case IN_NOTHING:
			return;
			
		case IN_RDF:
			if ("RDF".equals(localName)) {
				state = IN_NOTHING;
				return;
			}
			return;

		case IN_CHANNEL:
			// System.out.println("state = "+states[state]);
			// System.out.println("END localName = " + localName);

			// switch down
			if ("channel".equals(localName)) {
				state = IN_RDF;
				return;
			}
			if ("title".equals(localName)) {
				getFeed().setTitle(text);
				return;
			}
			if ("description".equals(localName)) {
				getFeed().setSubtitle(HtmlCleaner.stripTags(text));
				return;
			}
			if ("date".equals(localName)) {
				initDateStamp(getFeed());
				getFeed().getDateStamp().setSortDate(text);
				getFeed().getDateStamp().setUpdated(text);
				return;
			}
			if ("link".equals(localName)) {
				Link link = new LinkImpl();
				link.setHref(text);
					getFeed().setHtmlUrl(link.getHref());
				return;
			}

			return;

		case IN_ITEM:
			if ("item".equals(localName)) {
				state = IN_RDF;
				getFeed().addEntry(currentEntry);
				// System.out.println("DONE ENTRY = "+currentEntry);
				return;
			}
			if ("title".equals(localName)) {
				currentEntry.setTitle(text);
				return;
			}
			if ("description".equals(localName)) {
				String summary = text;
						// HtmlCleaner.unescape(text);
				//content = HtmlCleaner.normalise(content);
				currentEntry.setSummary(summary);
				Set<Link> links = HtmlCleaner.extractLinks(summary);
				currentEntry.addAllLinks(links);
				return;
			}
			if ("encoded".equals(localName)) {
				System.out.println("TEXT = "+text);
				String content = HtmlCleaner.unescape(text);
				System.out.println("CONTNT = "+content);
				// content = HtmlCleaner.normalise(content);
				currentEntry.setContent(content);
				Set<Link> links = HtmlCleaner.extractLinks(content);
				currentEntry.addAllLinks(links);
				return;
			}
			if ("author".equals(localName)) {
				initAuthor(currentEntry);
				currentEntry.getAuthor().setEmail(text);
				return;
			}
			if ("creator".equals(localName)) { // dc:creator, escaped - used by WordPress
				initAuthor(currentEntry);
				text = HtmlCleaner.unescape(text);
				currentEntry.getAuthor().setName(text);
				return;
			}
			if ("date".equals(localName)) {
				initDateStamp(currentEntry);
				currentEntry.getDateStamp().setSortDate(text);
				currentEntry.getDateStamp().setUpdated(text);
				return;
			}
			if ("link".equals(localName)) {
				currentEntry.setUrl(text);
				return;
			}
			return;

		default:
			return;
		}
	}
}

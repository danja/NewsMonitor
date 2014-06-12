/**
 * feedreader-prototype
 *
 * AtomHandler.java
 * 
 * @author danja
 * @date Apr 27, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.danja.feedreader.model.Entry;
import org.danja.feedreader.model.EntryList;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Person;
import org.danja.feedreader.model.impl.EntryImpl;
import org.danja.feedreader.model.impl.EntryListImpl;
import org.danja.feedreader.model.impl.LinkImpl;
import org.danja.feedreader.model.impl.PersonImpl;
import org.danja.feedreader.utils.DateConverters;
import org.danja.feedreader.utils.HtmlCleaner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for RSS 2.0 XML
 * <p>
 * Populates a {@link Feed} object (with any contained items etc.).
 * <p>
 * Elements handled : webMaster, author, creator, guid, title, "pubDate",
 * "link", "description"
 * <p>
 * HTML content has a little cleaning/normalization, and links are pulled out of
 * there too.
 * 
 * @see XMLReaderParser
 * @see Feed
 * @see Entry
 * @see Person
 * @see DateStamp
 * @see Link
 */

public class Rss2Handler extends FeedHandlerBase {

	// change to enum?
	private final static char IN_NOTHING = 0;
	private final static char IN_CHANNEL = 1;
	private final static char IN_ITEM = 2;
	private final static char IN_MEDIA = 3; // partially supported to avoid spurious titles etc.

	private static final String MEDIA_NS = "http://search.yahoo.com/mrss/";
	// handy for debugging
	private final static String[] states = { "IN_NOTHING", "IN_CHANNEL",
			"IN_ITEM" };

	private char state = IN_NOTHING;

	private StringBuffer textBuffer;

	private Entry currentEntry;

	// private EntryList entries = new EntryListImpl();
	private static final String[] textElementsArray = { "webMaster", "author",
			"creator", "date", "guid", "title", "pubDate", "lastBuildDate",
			"link", "description", "encoded" };
	private static final Set<String> textElements = new HashSet<String>();
	static {
		Collections.addAll(textElements, textElementsArray);
	}

	private Attributes attributes;

	public Rss2Handler() {
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
			if ("channel".equals(localName)) {
				state = IN_CHANNEL;
				return;
			}
			return;

		case IN_CHANNEL:
			if ("item".equals(localName)) {
				state = IN_ITEM;
				currentEntry = new EntryImpl();
				return;
			}
			return;
			
		case IN_ITEM:
			if (MEDIA_NS.equals(namespaceURI) && "content".equals(localName)) {
				state = IN_MEDIA;
				return;
			}
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

		// System.out.println("END localName = " + localName);
		// System.out.println("state = " + states[state]);

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

		case IN_CHANNEL:
			// System.out.println("state = "+states[state]);
			// System.out.println("END localName = " + localName);

			// switch down
			if ("channel".equals(localName)) {
				state = IN_NOTHING;
				return;
			}
			if ("guid".equals(localName)) {
				getFeed().setId(text);
				if ("".equals(getFeed().getUrl()) && text.startsWith("http://")) { // id
																					// might
																					// be
																					// url,
																					// but
																					// favour
																					// alternate
																					// link
					getFeed().setUrl(text);
				}
				return;
			}
			if ("title".equals(localName)) {
				getFeed().setTitle(text);
				return;
			}
			if ("description".equals(localName)) {
				getFeed().setSubtitle(text);
				return;
			}
			if ("webMaster".equals(localName)) {
				initAuthor(getFeed());
				getFeed().getAuthor().setEmail(text);
				return;
			}
			if ("pubDate".equals(localName)) {
				initDateStamp(getFeed());
				String iso = DateConverters.ISO8601FromRFC822(text);
				getFeed().getDateStamp().setPublished(iso);
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
				// System.out.println("out of Entry");
				state = IN_CHANNEL;
				getFeed().addEntry(currentEntry);
				// System.out.println("DONE ENTRY = "+currentEntry);
				return;
			}
			if ("guid".equals(localName)) {
				currentEntry.setId(text);
				if ("".equals(currentEntry.getUrl())
						&& text.startsWith("http://")) {// id might be url, but
														// favour alternate link
					currentEntry.setUrl(text);
				}
				return;
			}
			if ("title".equals(localName)) {
				currentEntry.setTitle(text);
				return;
			}
			if ("description".equals(localName)) {
				String content = HtmlCleaner.unescape(text);

				// content = HtmlCleaner.normalise(content);
				if (currentEntry.getContent() != null) { // already loaded from content:encoded
					currentEntry.setSummary(content);
				} else {
					currentEntry.setContent(content);
				}
				Set<Link> links = HtmlCleaner.extractLinks(content);
				currentEntry.addAllLinks(links);
				return;
			}
			if ("encoded".equals(localName)) { // occurs in the wild
				
				if (currentEntry.getContent() != null) {
					currentEntry.setSummary(currentEntry.getContent());
				}
				String content = HtmlCleaner.unescape(text);
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
			if ("creator".equals(localName)) { // dc:creator, escaped - used by
												// WordPress
				text = HtmlCleaner.unescape(text);
				initAuthor(currentEntry);
				currentEntry.getAuthor().setName(text);
				return;
			}
			if ("pubDate".equals(localName)) {
				initDateStamp(currentEntry);
				String iso = DateConverters.ISO8601FromRFC822(text);
				currentEntry.getDateStamp().setPublished(iso);
				return;
			}
			if ("link".equals(localName)) {
				currentEntry.setUrl(text);
				return;
			}
			return;

		case IN_MEDIA:
			if ("content".equals(localName)) {
				state = IN_ITEM;
				return;
			}
			return;
			
		default:
			return;
		}
	}
}

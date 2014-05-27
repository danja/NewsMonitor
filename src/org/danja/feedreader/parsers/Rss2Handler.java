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
import org.danja.feedreader.feeds.impl.DateConverters;
import org.danja.feedreader.feeds.impl.EntryImpl;
import org.danja.feedreader.feeds.impl.EntryListImpl;
import org.danja.feedreader.feeds.impl.LinkImpl;
import org.danja.feedreader.feeds.impl.PersonImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for RSS 2.0 XML
 * <p>
 * Populates a {@link Feed} object (with any contained items etc.).
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

public class Rss2Handler extends FeedHandler {

	// private Feed feed;

	private String sourceURI = "";

	private String sourceTitle = "";

	private String author = "";

	// change to enum?
	private final static char IN_NOTHING = 0;
	private final static char IN_CHANNEL = 1;
	private final static char IN_ITEM = 2;

	// handy for debugging
	private final static String[] states = { "IN_NOTHING", "IN_CHANNEL",
			"IN_ITEM"};

	private char state = IN_NOTHING;

	private StringBuffer textBuffer;

	private Entry currentEntry;

	// private EntryList entries = new EntryListImpl();
	private static final String[] textElementsArray = { "webMaster", "author", "creator", "guid", "title",
			"pubDate", "lastBuildDate", "link", "description" };
	private static final Set<String> textElements = new HashSet<String>();
	static {
		Collections.addAll(textElements, textElementsArray);
	}

	private Attributes attributes;

	private Feed feed;

	/**
	 * @return the feed
	 */
	public Feed getFeed() {
		return feed;
	}

	/**
	 * @param feed
	 *            the feed to set
	 */
	public void setFeed(Feed feed) {
		this.feed = feed;
		String date = DateConverters.dateAsISO8601(new Date());
		feed.getDateStamp().setSeen(date);
	}

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
				if("".equals(getFeed().getUrl()) && text.startsWith("http://")) { // id might be url, but favour alternate link
					getFeed().setUrl(text);
				}
				return;
			}
			if ("title".equals(localName)) {
				getFeed().setTitle(text);
				return;
			}
			if ("webMaster".equals(localName)) {
				getFeed().getAuthor().setEmail(text);
				return;
			}
			if ("pubDate".equals(localName)) {
				getFeed().getDateStamp().setPublished(text);
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
				feed.addEntry(currentEntry);
				// System.out.println("DONE ENTRY = "+currentEntry);
				return;
			}
			if ("guid".equals(localName)) {
				currentEntry.setId(text);
				if("".equals(currentEntry.getUrl()) && text.startsWith("http://")) {// id might be url, but favour alternate link
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
				//content = HtmlCleaner.normalise(content);
				currentEntry.setContent(content);
				Set<Link> links = HtmlCleaner.extractLinks(content);
				currentEntry.addAllLinks(links);
				return;
			}
			if ("author".equals(localName)) {
				currentEntry.getAuthor().setEmail(text);
				return;
			}
			if ("creator".equals(localName)) { // dc:creator, escaped - used by WordPress
				text = HtmlCleaner.unescape(text);
				currentEntry.getAuthor().setName(text);
				return;
			}
			if ("pubDate".equals(localName)) {
				currentEntry.getDateStamp().setPublished(text);
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

	public void endDocument() throws SAXException {
		System.out.println("AtomHandler FEED = \n" + feed);
	}

}

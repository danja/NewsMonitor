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
import org.danja.feedreader.feeds.impl.DateStampImpl;
import org.danja.feedreader.feeds.impl.EntryImpl;
import org.danja.feedreader.feeds.impl.EntryListImpl;
import org.danja.feedreader.feeds.impl.LinkImpl;
import org.danja.feedreader.feeds.impl.PersonImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for Atom XML
 * <p>
 * Populates a {@link Feed} object (with any contained entries etc.).
 * <p>
 * Elements handled : (feed level) id, title, link, published, updated, author
 * (entry level) as feed, plus content
 * <p>
 * Titles have any HTML tags removed, and HTML content has a little
 * cleaning/normalization, and links are pulled out of there too.
 * 
 * TODO class attribute is getting through in content
 * 
 * @see XMLReaderParser
 * @see Feed
 * @see Entry
 * @see Person
 * @see DateStamp
 * @see Link
 */

public class AtomHandler extends FeedHandler {

	// private Feed feed;

	private String sourceURI = "";

	private String sourceTitle = "";

	private String author = "";

	// change to enum?
	private final static char IN_NOTHING = 0;
	private final static char IN_FEED = 1;
	private final static char IN_ENTRY = 2;
	private final static char IN_FEED_AUTHOR = 3;
	private final static char IN_ENTRY_AUTHOR = 4;
	private final static char IN_CONTENT = 5;

	// handy for debugging
	private final static String[] states = { "IN_NOTHING", "IN_FEED",
			"IN_ENTRY", "IN_FEED_AUTHOR", "IN_ENTRY_AUTHOR", "IN_CONTENT" };

	private char state = IN_NOTHING;

	private StringBuffer textBuffer;
	private StringBuffer labelBuffer;

	private Entry currentEntry;

	// private EntryList entries = new EntryListImpl();
	private static final String[] textElementsArray = { "id", "title",
			"updated", "name", "email", "content" };
	private static final Set<String> textElements = new HashSet<String>();
	static {
		Collections.addAll(textElements, textElementsArray);
	}

	private Attributes attributes;

	private Feed feed;

	private boolean IN_CONTENT_LINK = false;

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
		initDateStamp(this.feed);
		String now = DateConverters.dateAsISO8601(new Date());
		feed.getDateStamp().setSeen(now);
	}

	public AtomHandler() {
		textBuffer = new StringBuffer();
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attrs) {

		// System.out.println("start = "+localName);
		// System.out.println("startQ = "+qName);

		if (textElements.contains(localName)) {
			textBuffer = new StringBuffer();
		}
		if("a".equals(localName)) {
			IN_CONTENT_LINK  = true;
			labelBuffer = new StringBuffer();
		}

		attributes = attrs;

		switch (state) {

		case IN_NOTHING:
			if ("feed".equals(localName)) {
				state = IN_FEED;
				return;
			}
			return;

		case IN_FEED:
			if ("author".equals(localName)) {
				state = IN_FEED_AUTHOR;
				return;
			}
			if ("entry".equals(localName)) {
				state = IN_ENTRY;
				currentEntry = new EntryImpl();
				return;
			}
			return;

		case IN_ENTRY:
			if ("author".equals(localName)) {
				state = IN_ENTRY_AUTHOR;
				return;
			}

			if ("content".equals(localName)) {
				// System.out.println("into Content");
				state = IN_CONTENT;
				return;
			}
			return;

		case IN_CONTENT:
			String elementName = HtmlCleaner.normaliseElement(localName);
// System.out.println("elementName = "+elementName);
			StringBuffer attrBuffer = new StringBuffer();
			for (int i = 0; i < attributes.getLength(); i++) {
				String name = attributes.getLocalName(i);
				if (!HtmlCleaner.excludeAttributes.contains(name)) {
					attrBuffer.append(" " + name + "=" + "\""
							+ attributes.getValue(i) + "\"");
				}
			}
			textBuffer.append("<" + elementName + attrBuffer + ">");
			return;

		default:
			return;
		}
	}

	public void characters(char[] ch, int start, int length) {
		textBuffer.append(ch, start, length);
		if(IN_CONTENT_LINK) {
			labelBuffer.append(ch, start, length);
		}
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

		case IN_FEED:
			// System.out.println("state = "+states[state]);
			// System.out.println("END localName = " + localName);

			// switch down
			if ("feed".equals(localName)) {
				state = IN_NOTHING;
				return;
			}
			if ("id".equals(localName)) {
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
				getFeed().setTitle(HtmlCleaner.stripTags(text));
				return;
			}
			if ("published".equals(localName)) {
				initDateStamp(getFeed());
				getFeed().getDateStamp().setPublished(text);
				return;
			}
			if ("updated".equals(localName)) {
				initDateStamp(getFeed());
				getFeed().getDateStamp().setUpdated(text);
				return;
			}
			if ("link".equals(localName)) {
				Link link = new LinkImpl();
				String rel = attributes.getValue("rel");
				String href = attributes.getValue("href");
				String type = attributes.getValue("type");

				link.setRel(rel);
				link.setHref(href);
				link.setType(type);
				getFeed().addLink(link);

				// WordPress
				// <link rel="alternate" type="text/html"
				// href="http://longoio.wordpress.com" />
				// <id>http://longoio.wordpress.com/feed/atom/</id>
				// <link rel="self" type="application/atom+xml"
				// href="http://longoio.wordpress.com/feed/atom/" />

				// this seems to be a common usage,though the alternate would be
				// preferred
				if (getFeed().getHtmlUrl() == null && href != null
						&& !"".equals(href) && rel == null && type == null) {
					getFeed().setHtmlUrl(href);
				}
				if (link.isHtmlAlternate()) {
					getFeed().setHtmlUrl(link.getHref());
				}
				return;
			}
			return;

		case IN_FEED_AUTHOR:
			if ("name".equals(localName)) {
				initAuthor(getFeed());
				feed.getAuthor().setName(text);
				return;
			}
			if ("email".equals(localName)) {
				initAuthor(getFeed());
				feed.getAuthor().setEmail(text);
				return;
			}
			if ("author".equals(localName)) {
				initAuthor(getFeed());
				state = IN_FEED;
				return;
			}
			return;

		case IN_ENTRY:
			if ("entry".equals(localName)) {
				// System.out.println("out of Entry");
				state = IN_FEED;
				feed.addEntry(currentEntry);
				// System.out.println("DONE ENTRY = "+currentEntry);
				return;
			}
			if ("id".equals(localName)) {
				currentEntry.setId(text);
				if ("".equals(currentEntry.getUrl())
						&& text.startsWith("http://")) {// id might be url, but
														// favour alternate link
					currentEntry.setUrl(text);
				}
				return;
			}
			if ("title".equals(localName)) {
				currentEntry.setTitle(HtmlCleaner.stripTags(text));
				return;
			}
			if ("published".equals(localName)) {
				initDateStamp(currentEntry);
				currentEntry.getDateStamp().setPublished(text);
				return;
			}
			if ("updated".equals(localName)) {
				initDateStamp(currentEntry);
				currentEntry.getDateStamp().setUpdated(text);
				return;
			}
			if ("link".equals(localName)) {
				Link link = new LinkImpl();
				link.setRel(attributes.getValue("rel"));
				link.setHref(attributes.getValue("href"));
				link.setType(attributes.getValue("type"));
				currentEntry.addLink(link);
				if (link.isHtmlAlternate()) {
					currentEntry.setUrl(link.getHref());
				}
				return;
			}
			return;

		case IN_CONTENT:
			// System.out.println("element = "+localName);
			if ("content".equals(localName)
					&& "http://www.w3.org/2005/Atom".equals(namespaceURI)) {
				// System.out.println("content text = " + text);
				currentEntry.setContent(text);
				state = IN_ENTRY;
				return;
			}
			textBuffer.append("</" + localName + ">");

			// pull out links in content
			if("a".equals(localName)){
				
				
			for (int i = 0; i < attributes.getLength(); i++) {
				String name = attributes.getLocalName(i);
				if ("href".equals(name)) {
					Link link = new LinkImpl();
					link.setHref(attributes.getValue(i));
					link.setRel("related");
					String label = HtmlCleaner.stripTags(labelBuffer.toString()).trim();
					link.setLabel(label);
					currentEntry.addLink(link);
				}
			}
			}
			return;

		case IN_ENTRY_AUTHOR:
			if ("name".equals(localName)) {
				initAuthor(currentEntry);
				currentEntry.getAuthor().setName(text);
				return;
			}
			if ("email".equals(localName)) {
				initAuthor(currentEntry);
				currentEntry.getAuthor().setEmail(text);
				return;
			}
			if ("author".equals(localName)) {
				state = IN_ENTRY;
				return;
			}
			return;

		default:
			return;
		}
	}



	public void endDocument() throws SAXException {
		// System.out.println("AtomHandler FEED = \n" + feed);
	}

}

/**
 * NewsMonitor
 *
 * AtomHandler.java
 * 
 * @author danja
 * @date Apr 27, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Entry;
import it.danja.newsmonitor.model.EntryList;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.Person;
import it.danja.newsmonitor.model.impl.DateStampImpl;
import it.danja.newsmonitor.model.impl.EntryImpl;
import it.danja.newsmonitor.model.impl.EntryListImpl;
import it.danja.newsmonitor.model.impl.LinkImpl;
import it.danja.newsmonitor.model.impl.PersonImpl;
import it.danja.newsmonitor.utils.ContentType;
import it.danja.newsmonitor.utils.DateConverters;
import it.danja.newsmonitor.utils.HtmlCleaner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
 * <p>
 * Currently uses entry HTML URL as URI, not precise but pragmatic - maybe TODO split url/htmlURL
 * <p>
 * TODO class attribute is getting through in content ? fixed?
 * TODO support subtitle, summary
 * 
 * @see XMLReaderParser
 * @see Feed
 * @see Entry
 * @see Person
 * @see DateStamp
 * @see Link
 */

public class AtomHandler extends FeedHandlerBase {

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
	private final static char IN_SUMMARY = 6;

	// handy for debugging
	private final static String[] states = { "IN_NOTHING", "IN_FEED",
			"IN_ENTRY", "IN_FEED_AUTHOR", "IN_ENTRY_AUTHOR", "IN_CONTENT", "IN_SUMMARY" };

	private char state = IN_NOTHING;

	private StringBuffer textBuffer;
	private StringBuffer labelBuffer;

	private Entry currentEntry;

	// private EntryList entries = new EntryListImpl();
	private static final String[] textElementsArray = { "id", "title", "subtitle",
			"updated", "name", "uri", "email", "content", "summary" };
	private static final Set<String> textElements = new HashSet<String>();
	static {
		Collections.addAll(textElements, textElementsArray);
	}

	private Attributes attributes;

	private boolean IN_CONTENT_LINK = false;

	/**
	 * @param feed
	 *            the feed to set
	 */


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
		if ("a".equals(localName)) {
			IN_CONTENT_LINK = true;
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
				currentEntry.setFeedUrl(getFeed().getUrl());
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
			if ("summary".equals(localName)) {
				// System.out.println("into Content");
				state = IN_SUMMARY;
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
			
		case IN_SUMMARY:
			String eName = HtmlCleaner.normaliseElement(localName);
			// System.out.println("elementName = "+elementName);
			StringBuffer attrBuff = new StringBuffer();
			for (int i = 0; i < attributes.getLength(); i++) {
				String name = attributes.getLocalName(i);
				if (!HtmlCleaner.excludeAttributes.contains(name)) {
					attrBuff.append(" " + name + "=" + "\""
							+ attributes.getValue(i) + "\"");
				}
			}
			textBuffer.append("<" + eName + attrBuff + ">");
			return;

		default:
			return;
		}
	}

	public void characters(char[] ch, int start, int length) {
		textBuffer.append(ch, start, length);
		if (IN_CONTENT_LINK) {
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
				return;
			}
			if ("title".equals(localName)) {
				getFeed().setTitle(HtmlCleaner.stripTags(text));
				return;
			}
			if ("subtitle".equals(localName)) {
				getFeed().setSubtitle(HtmlCleaner.stripTags(text));
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
				link.setOrigin(getFeed().getUrl());
				String rel = attributes.getValue("rel");
				String href = attributes.getValue("href");
				String type = attributes.getValue("type");

				link.setRel(rel);
				link.setHref(href);
				
				link.setFormat(ContentType.getTypeName(type));
				link.setContentType(type);
				
				getFeed().addLink(link);

				// WordPress
				// <link rel="alternate" type="text/html"
				// href="http://longoio.wordpress.com" />
				// <id>http://longoio.wordpress.com/feed/atom/</id>
				// <link rel="self" type="application/atom+xml"
				// href="http://longoio.wordpress.com/feed/atom/" />

				// this seems to be a common usage,though the alternate is
				// preferred
				if (getFeed().getHtmlUrl() == null && href != null
						&& !"".equals(href) && rel == null && type == null) {
					getFeed().setHtmlUrl(href);
				}
				// best
				if (rel != null && rel.equals("alternate") && type != null
						&& "text/html".equals(type)) {
					getFeed().setHtmlUrl(href);
				}
				return;
			}
			return;

		case IN_FEED_AUTHOR:
			if ("name".equals(localName)) {
				initAuthor(getFeed());
				getFeed().getAuthor().setName(text);
				return;
			}
			if ("email".equals(localName)) {
				initAuthor(getFeed());
				getFeed().getAuthor().setEmail(text);
				return;
			}
			if ("uri".equals(localName)) {
				initAuthor(getFeed());
			//	System.out.println("URI = "+text);
				getFeed().getAuthor().setHomepage(text);
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
				getFeed().addEntry(currentEntry);
				// System.out.println("DONE ENTRY = "+currentEntry);
				return;
			}
			if ("id".equals(localName)) {
				currentEntry.setId(text);
				// id might be url, but favour alternate link
				if (currentEntry.getUrl() == null && text.startsWith("http://")) {							
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

				String rel = attributes.getValue("rel");
				String href = attributes.getValue("href");
				String type = attributes.getValue("type");
				if (href == null || "".equals(href)) { // useless
					return;
				}

				try {
					href = HtmlCleaner.resolveUrl(getFeed().getUrl(), href);
				} catch (URISyntaxException e) {
					href = null;
				}

				Link link = new LinkImpl();
				link.setOrigin(getFeed().getUrl());
				link.setRel(rel);
				link.setHref(href);
				
				link.setContentType(type);
				link.setFormat(ContentType.getTypeName(type));
				
				currentEntry.addLink(link);

				// best
				if (rel != null && rel.equals("alternate") && type != null
						&& "text/html".equals(type)) {
					currentEntry.setUrl(href);
				}
				// good
				if (currentEntry.getUrl() == null && rel == null
						&& type != null && "text/html".equals(type)) {
					currentEntry.setUrl(href);
				}
				// ok
				if (currentEntry.getUrl() == null && rel == null
						&& type == null) {
				//	System.out.println("SETTING URL TO "+href);
					currentEntry.setUrl(href);
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
			if ("a".equals(localName)) {

				for (int i = 0; i < attributes.getLength(); i++) {
					String name = attributes.getLocalName(i);
// System.out.println("NAME = "+name);
					if ("href".equals(name)) {
						Link link = new LinkImpl();
						link.setOrigin(getFeed().getUrl());
						link.setRel("related");
						link.setHref(attributes.getValue(i));
						String label = HtmlCleaner.stripTags(
								labelBuffer.toString()).trim();
						link.setLabel(label);
						currentEntry.addLink(link);
					}
				}
			}
			return;

		case IN_SUMMARY:
			// System.out.println("element = "+localName);
			if ("summary".equals(localName)
					&& "http://www.w3.org/2005/Atom".equals(namespaceURI)) {
				// System.out.println("content text = " + text);
				currentEntry.setSummary(text);
				// System.out.println("SUMMARY = "+text);
				// System.out.println("currentEntry.getSummary() = "+currentEntry.getSummary());
				state = IN_ENTRY;
				return;
			}
			textBuffer.append("</" + localName + ">");

			// pull out links in content
			if ("a".equals(localName)) {

				for (int i = 0; i < attributes.getLength(); i++) {
					String name = attributes.getLocalName(i);

					if ("href".equals(name)) {
						Link link = new LinkImpl();
						link.setOrigin(getFeed().getUrl());
						link.setRel("related");
						link.setHref(attributes.getValue(i));
						String label = HtmlCleaner.stripTags(
								labelBuffer.toString()).trim();
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
			if ("uri".equals(localName)) {
				initAuthor(currentEntry);
				currentEntry.getAuthor().setHomepage(text);
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
}

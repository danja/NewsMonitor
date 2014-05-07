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

import org.danja.feedreader.feeds.Entry;
import org.danja.feedreader.feeds.EntryImpl;
import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedImpl;
import org.danja.feedreader.feeds.Person;
import org.danja.feedreader.feeds.PersonImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for RSS 2.0 XML
 * 
 */

public class AtomHandler extends FeedHandler {
	
	//private Feed feed;

	private String sourceURI = "";

	private String sourceTitle = "";

	private String author = "";

	private final static char IN_NOTHING = 0;

	private final static char IN_FEED = 1;

	private final static char IN_ENTRY = 2;

	private final static char IN_AUTHOR = 3;

	private char state = IN_NOTHING;

	private StringBuffer textBuffer;

	private Entry entry;

	private EntryList entries = new EntryListImpl();

	private Attributes attributes;

	private Person person = new PersonImpl();

	// TODO used?
	private String feedTitle = "";
	private String feedLink = "";

//	private FeedImpl feed;

	public AtomHandler() {
		textBuffer = new StringBuffer();
	}
	
	


	
	public void startDocument() throws SAXException {
	//	System.out.println("AtomHandler.startDocument()");
	}

//	public void setEntryList(EntryList entries) {
//		this.entries = entries;
//	}

	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attrs) {

	//	System.out.println("startElement");
		attributes = attrs;
		textBuffer = new StringBuffer();

		switch (state) {

		case IN_NOTHING:
			if ("feed".equals(localName)) {
				state = IN_FEED;
			}
			// feed = new FeedImpl();
			return;

		case IN_FEED: 
			
			if ("entry".equals(localName)) {
				state = IN_ENTRY;
			}
			entry = new EntryImpl();
			
			textBuffer = new StringBuffer();
			return;

		case IN_ENTRY:
			if ("author".equals(localName)) {
				state = IN_AUTHOR;
			}
			return;

		default:
			return;
		}
	}

	public void characters(char[] ch, int start, int length) {
		textBuffer.append(ch, start, length);
	}

	public void endElement(String namespaceURI, String localName, String qName) {
		
		System.out.println("localName = "+localName);

		switch (state) {

		case IN_NOTHING:
			return;

		case IN_FEED: 
			
			// switch down
			if ("feed".equals(localName)) {
				state = IN_NOTHING;
			}
			
			if ("title".equals(localName)) {
				getFeed().setTitle(textBuffer.toString());
				return;
			}

			if ("link".equals(localName)) {
				System.out.println("LINK attrs = " + attributes);
				System.out.println("LINK");
				// feed.setLink(localName);
			}

			// added for Poller
			if ("title".equals(localName)) {
				feedTitle = textBuffer.toString();
				return;
			}
			return;

		case IN_ENTRY:
			if ("entry".equals(localName)) {
				state = IN_FEED;
				entries.addEntry(entry);
				return;
			}
			if ("title".equals(localName)) {
				entry.setTitle(textBuffer.toString());
				return;
			}
			if ("content".equals(localName)) {
				System.out.println("in AtomHandler textBuffer.toString() = "
						+ textBuffer.toString());
				entry.setContent(textBuffer.toString());
				return;
			}

			// added for Poller
			if ("guid".equals(localName)) {
				entry.setUrl(textBuffer.toString());
				return;
			}
			if ("pubDate".equals(localName)) {
				entry.setDate(textBuffer.toString());
				return;
			}

			if ("link".equals(localName)) {
				entry.setLink(textBuffer.toString());
				return;
			}

			// if ("entry".equals(localName)) {
			// state = IN_ENTRY;
			// entry = new EntryImpl();
			// // added for Poller
			//
			// entry.setAuthor(author);
			// entry.setSourceTitle(feedTitle);
			// entry.setSourceLink(feedLink);
			// }

			textBuffer.append(localName);
			return;

		case IN_AUTHOR:
			if ("author".equals(localName)) {
				// feed.setAuthor(person);
				System.out.println("Person = "+person);
			}
			if ("name".equals(localName)) {
				person.setName(textBuffer.toString());
			}
			if ("email".equals(localName)) {
				person.setEmail(textBuffer.toString());
			}
			return;
			
		default:
			return;
		}
	}

	public void endDocument() throws SAXException {
		System.out.println("AtomHandler entry = " + entry);
	}

}

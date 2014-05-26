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
import org.danja.feedreader.feeds.Person;
import org.danja.feedreader.feeds.impl.DateConverters;
import org.danja.feedreader.feeds.impl.EntryImpl;
import org.danja.feedreader.feeds.impl.EntryListImpl;
import org.danja.feedreader.feeds.impl.PersonImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler for Atom XML
 * 
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
	
	private final static String[] states = {"IN_NOTHING", "IN_FEED", "IN_ENTRY", "IN_FEED_AUTHOR", "IN_ENTRY_AUTHOR", "IN_CONTENT" };
	
	private char state = IN_NOTHING;

	private StringBuffer textBuffer;

	private Entry currentEntry;

	// private EntryList entries = new EntryListImpl();
	private static final String[] textElementsArray = { "title", "updated",
			"name", "email", "content" };
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
				
				// System.out.println("into Entry"+namespaceURI);
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
			
			StringBuffer attrBuffer = new StringBuffer();
			for(int i = 0;i<attributes.getLength();i++) {
				String name = attributes.getLocalName(i);
				if(!HtmlCleaner.excludeAttributes.contains(name)){
					attrBuffer.append(" "+name+"="+"\""+attributes.getValue(i)+"\"");
				}
			}
			textBuffer.append("<"+elementName+attrBuffer+">");
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
		
		 System.out.println("END localName = " + localName);
		 System.out.println("state = "+states[state]);

		String text = "";
		if (textElements.contains(localName))  {
			text = textBuffer.toString();
			if(text.length() > 0) {
			text = text.trim();
			}
		}

		switch (state) {

		case IN_NOTHING:
			return;

		case IN_FEED:
			 System.out.println("state = "+states[state]);
			 System.out.println("END localName = " + localName);
			 
			// switch down
			if ("feed".equals(localName)) {
				System.out.println("DONE. Feed = \n");
				state = IN_NOTHING;
				System.out.println("DONE. Feed = \n"+feed);
				return;
			}
			if ("title".equals(localName)) {
				getFeed().setTitle(text);
				return;
			}
			if ("published".equals(localName)) {
				getFeed().getDateStamp().setPublished(text);
				return;
			}
			if ("updated".equals(localName)) {
				getFeed().getDateStamp().setUpdated(text);
				return;
			}
			if ("link".equals(localName)) {
				// System.out.println("LINK attrs = " + attributes);
				// .out.println("LINK");
				// feed.setLink(localName);
				return;
			}
			return;

		case IN_FEED_AUTHOR:
			if ("name".equals(localName)) {
				feed.getAuthor().setName(text);
				return;
			}
			if ("email".equals(localName)) {
				feed.getAuthor().setEmail(text);
				return;
			}
			if ("author".equals(localName)) {
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
			if ("title".equals(localName)) {
				currentEntry.setTitle(text);
				return;
			}
			if ("published".equals(localName)) {
				currentEntry.getDateStamp().setPublished(text);
				return;
			}
			if ("updated".equals(localName)) {
				currentEntry.getDateStamp().setUpdated(text);
				return;
			}
			if ("link".equals(localName)) {
				// currentEntry.setLink(textBuffer.toString().trim());
				return;
			}
			return;
			

		case IN_CONTENT:
			if ("content".equals(localName) && "http://www.w3.org/2005/Atom".equals(namespaceURI)) {
			//	System.out.println("content text = " + text);
				currentEntry.setContent(text);
				state = IN_ENTRY;
				return;
			}
			textBuffer.append("</"+localName+">");
			return;

		case IN_ENTRY_AUTHOR:
			if ("name".equals(localName)) {
				currentEntry.getAuthor().setName(text);
				return;
			}
			if ("email".equals(localName)) {
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
		System.out.println("AtomHandler FEED = \n" + feed);
	}

}

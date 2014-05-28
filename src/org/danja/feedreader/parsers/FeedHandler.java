/**
 * feedreader-prototype
 *
 * FeedHandler.java
 * @author danja
 * @date Apr 28, 2014
 *
 */
package org.danja.feedreader.parsers;

import org.danja.feedreader.feeds.Feed;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
public abstract class FeedHandler implements ContentHandler {

	private Feed feed;

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public Feed getFeed() {
		return feed;
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// no operation

	}

	@Override
	public void startDocument() throws SAXException {
	//	System.out.println("FeedHandler.startDocument()");
	}

//	@Override
//	public void endDocument() throws SAXException {
//		// no operation
//
//	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// no operation

	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// no operation

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// no operation

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// no operation

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// no operation

	}
}

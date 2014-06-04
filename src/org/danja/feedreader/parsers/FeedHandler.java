/**
 * feedreader-prototype
 *
 * FeedHandler.java
 * @author danja
 * @date Apr 28, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.danja.feedreader.feeds.DateStamp;
import org.danja.feedreader.feeds.Entry;
import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedEntity;
import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.Person;
import org.danja.feedreader.feeds.impl.DateStampImpl;
import org.danja.feedreader.feeds.impl.PersonImpl;
import org.danja.feedreader.utils.DateConverters;
import org.danja.feedreader.utils.HtmlCleaner;
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
		initDateStamp(this.feed);
		String now = DateConverters.dateAsISO8601(new Date());
		feed.getDateStamp().setSeen(now);
	}

	public Feed getFeed() {
		return feed;
	}
	
	// want these as null unless they are populated
	protected void initDateStamp(FeedEntity feedEntity) {
		if(feedEntity.getDateStamp() == null) {
			feedEntity.setDateStamp(new DateStampImpl());
		}
		
	}

	protected void initAuthor(FeedEntity feedEntity) {
		if(feedEntity.getAuthor() == null) {
			feedEntity.setAuthor(new PersonImpl());
		}
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
	
	public void endDocument() throws SAXException {
		resolveContent();
		resolveLinks();
		resolveAuthor();
		resolveDate();
	}

	protected void resolveContent() {
		List<Entry> entries = feed.getEntries().getEntries();
		for(int i= 0;i<entries.size();i++){
			if(entries.get(i).getContent() == null && entries.get(i).getSummary() != null) {
				entries.get(i).setContent(entries.get(i).getSummary());
			}
		}
	}
	
	protected void resolveDate() {
		chooseDate(feed.getDateStamp());
		
		List<Entry> entries = feed.getEntries().getEntries();
		for(int i= 0;i<entries.size();i++){
			if(entries.get(i).getDateStamp() != null) {
				chooseDate(entries.get(i).getDateStamp());
			}
		}
	}
	
	private void chooseDate(DateStamp dateStamp){
		if(dateStamp.getUpdated() != null) { // best
			dateStamp.setSortDate(dateStamp.getUpdated());
		}
		if(dateStamp.getSortDate() == null && dateStamp.getPublished() != null) { // ok approx
			dateStamp.setSortDate(dateStamp.getPublished());
		}	
		if(dateStamp.getSortDate() == null) { // FALLBACK
			dateStamp.setToFallback();
		}	
		System.out.println("DATESTAMP = "+dateStamp);
	}
	
	/**
	 * Checks if each entry has an author, if not use the feed author (if one exists)
	 * also makes feed author URL absolute
	 */
	protected void resolveAuthor() {
		List<Entry> entries = feed.getEntries().getEntries();
		Person feedAuthor = feed.getAuthor();
		if(feedAuthor == null){
			return;
		}
		feedAuthor.setHomepage(HtmlCleaner.resolveUrl(feed.getUrl(),feedAuthor.getHomepage()));
//		String name = feedAuthor.getName();
//		String homepage = feedAuthor.getHomepage();
//		String email = feedAuthor.getEmail();
		
		for(int i= 0;i<entries.size();i++){
			if(entries.get(i).getAuthor() == null) {
			//	System.out.println("AUTHor = "+feedAuthor);
				entries.get(i).setAuthor(feedAuthor);
			}
		}
	}
	

	protected void resolveLinks() {
		System.out.println("LINKS **************************"+feed.getLinks());
		Iterator<Link> iterator = feed.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = iterator.next();
			link.setHref(HtmlCleaner.resolveUrl(feed.getUrl(),
					link.getHref()));
		}
		EntryList entries = feed.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			Entry currentEntry = entries.getEntry(i);
			Iterator<Link> iterator2 = currentEntry.getLinks().iterator();
			while (iterator2.hasNext()) {
				Link link = iterator2.next();
			//	System.out.println("LINK="+link);
				link.setHref(HtmlCleaner.resolveUrl(feed.getUrl(),
						link.getHref()));
			}
		}
	}
}

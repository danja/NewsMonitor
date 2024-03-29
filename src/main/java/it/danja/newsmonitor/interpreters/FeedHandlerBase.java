/**
 * NewsMonitor
 *
 * FeedHandlerBase.java
 * @author danja
 * dc:date Apr 28, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import it.danja.newsmonitor.model.DateStamp;
import it.danja.newsmonitor.model.Entry;
import it.danja.newsmonitor.model.EntryList;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedEntity;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.Person;
import it.danja.newsmonitor.model.impl.DateStampImpl;
import it.danja.newsmonitor.model.impl.PersonImpl;
import it.danja.newsmonitor.utils.CharsetDetector;
import it.danja.newsmonitor.utils.ContentProcessor;
import it.danja.newsmonitor.utils.DateConverters;
import it.danja.newsmonitor.utils.HtmlCleaner;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
public abstract class FeedHandlerBase implements FeedHandler {

  private Feed feed;

  /*
   * (non-Javadoc)
   *
   * @see
   * it.danja.newsmonitor.interpreters.FeedHandler#setFeed(org.danja.feedreader
   * .model.Feed)
   */
  @Override
  public void setFeed(Feed feed) {
    this.feed = feed;
    initDateStamp(this.feed);
    String now = DateConverters.dateAsISO8601(new Date());
    feed.getDateStamp().setSeen(now);
  }

  /*
   * (non-Javadoc)
   *
   * @see it.danja.newsmonitor.interpreters.FeedHandler#getFeed()
   */
  @Override
  public Feed getFeed() {
    return feed;
  }

  // want these as null unless they are populated
  protected void initDateStamp(FeedEntity feedEntity) {
    if (feedEntity.getDateStamp() == null) {
      feedEntity.setDateStamp(new DateStampImpl());
    }
  }

  protected void initAuthor(FeedEntity feedEntity) {
    if (feedEntity.getAuthor() == null) {
      feedEntity.setAuthor(new PersonImpl());
    }
  }

  @Override
  public void setDocumentLocator(Locator locator) {
    // no operation

  }

  @Override
  public void startDocument() throws SAXException {
    // log.info("FeedHandlerBase.startDocument()");
  }

  // @Override
  // public void endDocument() throws SAXException {
  // // no operation
  //
  // }

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
    //	System.out.println("RESOLVE CONTENT");
    List<Entry> entries = feed.getEntries().getEntries();
    //	System.out.println("RESOLVE CONTENT for "+entries.size()+" entries");
    for (int i = 0; i < entries.size(); i++) {
      if (
        entries.get(i).getContent() == null &&
        entries.get(i).getSummary() != null
      ) {
        entries.get(i).setContent(entries.get(i).getSummary());
      }

      entries
        .get(i)
        .setContent(CharsetDetector.fixEncoding(entries.get(i).getContent()));
      //	System.out.println("pre unescape "+entries.get(i).getContent());
      entries
        .get(i)
        .setContent(ContentProcessor.unescape(entries.get(i).getContent()));

      //		System.out.println("post unescape "+entries.get(i).getContent());
      Set<Link> contentLinks = ContentProcessor.extractLinks(
        getFeed(),
        entries.get(i).getContent()
      );

      // 2023
      String markdown = FlexmarkHtmlConverter
        .builder()
        .build()
        .convert(entries.get(i).getContent());
      entries.get(i).setContent(markdown);

      //	System.out.println("contentLinks.size "+contentLinks.size());
      // feed.addAllLinks(contentLinks); // TODO is ok?
      entries.get(i).addAllLinks(contentLinks);
      //	System.out.println("entries.get(i) "+entries.get(i));
    }
  }

  protected void resolveDate() {
    chooseDate(feed.getDateStamp());

    List<Entry> entries = feed.getEntries().getEntries();
    for (int i = 0; i < entries.size(); i++) {
      if (entries.get(i).getDateStamp() != null) {
        chooseDate(entries.get(i).getDateStamp());
      }
    }
  }

  private void chooseDate(DateStamp dateStamp) {
    if (dateStamp.getUpdated() != null) { // best
      dateStamp.setSortDate(dateStamp.getUpdated());
    }
    if (dateStamp.getSortDate() == null && dateStamp.getPublished() != null) { // ok
      // approx
      dateStamp.setSortDate(dateStamp.getPublished());
    }
    if (dateStamp.getSortDate() == null) { // FALLBACK
      dateStamp.setToFallback();
    }
    // log.info("DATESTAMP = "+dateStamp);
  }

  /**
   * Checks if each entry has an author, if not use the feed author (if one
   * exists) also makes feed author URL absolute
   */
  protected void resolveAuthor() {
    List<Entry> entries = feed.getEntries().getEntries();
    Person feedAuthor = feed.getAuthor();
    if (feedAuthor == null) {
      return;
    }
    try {
      feedAuthor.setHomepage(
        HtmlCleaner.resolveUrl(feed.getUrl(), feedAuthor.getHomepage())
      );
    } catch (URISyntaxException e) {
      // ignore
    }
    String name = feedAuthor.getName();
    String homepage = feedAuthor.getHomepage();
    String email = feedAuthor.getEmail();
    if (name != null) {
      feedAuthor.setName(name);
    }
    if (homepage != null) {
      feedAuthor.setName(homepage);
    }
    if (email != null) {
      feedAuthor.setName(email);
    }
    for (int i = 0; i < entries.size(); i++) {
      if (entries.get(i).getAuthor() == null) {
        // log.info("AUTHor = "+feedAuthor);
        entries.get(i).setAuthor(feedAuthor);
      }
    }
  }

  protected void resolveLinks() {
    // log.info("Links "+feed.getLinks().size());
    Iterator<Link> iterator = feed.getAllLinks().iterator();
    // String feedUrl = feed.getUrl();
    // String[] split = feedUrl.split()
    // Set<Link> broken = new HashSet<Link>();
    Set<Link> broken = Collections.newSetFromMap(
      new ConcurrentHashMap<Link, Boolean>()
    );
    while (iterator.hasNext()) {
      Link link = iterator.next();
      String resolved = null;
      try {
        resolved = HtmlCleaner.resolveUrl(feed.getUrl(), link.getHref());
        // log.info("RESOLVED = "+resolved);
      } catch (URISyntaxException e) {
        broken.add(link);
        continue;
      }
      link.setHref(resolved);
      link.setOrigin(getFeed().getUrl());
      //	log.info("LINK = " + link);
      if (link.getLabel() != null) {
        String raw = link.getLabel();
        String cleaned = HtmlCleaner.stripTags(raw).trim();

        if (cleaned.length() == 0 && raw.indexOf("<img") != -1) {
          link.setLabel("[image]");
        } else {
          link.setLabel(cleaned);
        }
      }
    }

    Iterator<Link> brokenIterator = broken.iterator();
    while (brokenIterator.hasNext()) {
      feed.getLinks().remove(brokenIterator.next());
    }

    EntryList entries = feed.getEntries();
    for (int i = 0; i < entries.size(); i++) {
      Entry currentEntry = entries.getEntry(i);
      Iterator<Link> iterator2 = currentEntry.getLinks().iterator();
      while (iterator2.hasNext()) {
        Link link = iterator2.next();
        try {
          link.setHref(HtmlCleaner.resolveUrl(feed.getUrl(), link.getHref()));
        } catch (URISyntaxException e) {
          // ignore
        }
      }
    }
  }
}

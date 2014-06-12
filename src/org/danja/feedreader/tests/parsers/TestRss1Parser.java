package org.danja.feedreader.tests.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Entry;
import org.danja.feedreader.model.FeedConstants;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.impl.FeedImpl;
import org.danja.feedreader.templating.Templater;
import org.danja.feedreader.utils.HttpServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRss1Parser {

	private final String url = "http://localhost:8080/test-data/rss1-sample.xml";
	private final static String rootDir = "www";
	private FeedImpl feed;
	private Interpreter interpreter;
	private static HttpServer server = new HttpServer(rootDir, 8080);

	@BeforeClass
	public static void startServer() {
		server.init();
		server.start();
	}

	@Before
	public void setUp() throws Exception {

		feed = new FeedImpl();
		feed.setUrl(url);
		feed.setFormatHint(FeedConstants.RSS1);
		interpreter = InterpreterFactory.createInterpreter(feed);
		feed.setInterpreter(interpreter);
		feed.refresh();
		Templater.init();
		String feedTurtle = Templater.apply("feed-turtle-no-prefixes", feed.getTemplateDataMap());
		System.out.println("# Feed Turtle\n"+feedTurtle);
	}

	@Test
	public void testFeedLevel() {
		assertEquals("checking feed title", "Feed Title", feed.getTitle());
		assertEquals("checking feed subtitle", "Feed subtitle", feed.getSubtitle());
		assertEquals("checking feed id",
				"http://longoio.wordpress.com", feed.getId());
		assertEquals("checking feed url", url, feed.getUrl());
		assertEquals("checking entry count", 2, feed.getEntries().size());
		assertEquals("checking HTML url", "http://www.example.com/main.html",
				feed.getHtmlUrl());
		assertEquals("checking date", "2014-05-27T11:18:15Z",
				feed.getDateStamp().getUpdated());
		// TODO implement -note, canhave multiple links
		// assertEquals("checking feed link", , feed.getLink());
	}

	// webMaster, author, creator, guid, title,
	// "pubDate", , "link", "description"

	@Test
	public void testEntryLevel() {
		Entry entry0 = feed.getEntries().getEntry(0);
		assertEquals("checking entry id",
				"http://example.org/entry1", entry0.getId());
	
		assertEquals("checking entry title", "Entry 1", entry0.getTitle());
		assertEquals("checking entry 1 summary", "<p>Entry 1 summary</p>", entry0.getSummary());
		assertEquals("checking entry content", "<p>Entry 1 content</p>",
				entry0.getContent());
		assertEquals("checking entry url", "http://example.org/entry1",
				entry0.getUrl());
		String updated = entry0.getDateStamp().getUpdated();
		assertEquals("checking entry date",
				"2014-05-27T09:30:55Z", updated);
		
		Entry entry1 = feed.getEntries().getEntry(1);
		assertEquals("checking entry2 URL", "http://example.org/entry2", entry1.getUrl());
		assertEquals("checking entry2 date", "2014-05-27T07:10:43Z", entry1.getDateStamp().getSortDate());
		// <p>Entry 2 content <a href="http://example.com">with a link</a></p>
		Set<Link> links = entry1.getLinks();
		boolean found = false;
		Iterator<Link> i = links.iterator();
		while (i.hasNext()) {
			Link link = i.next();
			// System.out.println("link: " + link);
			if ("http://example.com".equals(link.getHref())
					&& "with a link".equals(link.getLabel())) {
				found = true;
			//	System.out.println("TRUE: " + link);
			}
		}
		assertTrue("checking link in content", found);
	}

	@After
	public void tearDown() {
	}

	@AfterClass
	public static void stopServer() {
		server.stop();
	}
}

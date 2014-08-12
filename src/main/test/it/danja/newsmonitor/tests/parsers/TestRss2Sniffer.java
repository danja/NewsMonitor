package it.danja.newsmonitor.tests.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.danja.newsmonitor.interpreters.Interpreter;
import it.danja.newsmonitor.interpreters.InterpreterFactory;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Entry;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.FeedImpl;
import it.danja.newsmonitor.templating.Templater;
import it.danja.newsmonitor.tests.util.HttpServer;
import it.danja.newsmonitor.utils.ContentType;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRss2Sniffer {

	private final String url = "http://localhost:8080/test-data/rss2-sample.xml";
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
		feed.init();
		feed.refresh();
		Templater.init();
		String feedTurtle = Templater.apply("feed-turtle-no-prefixes",
				feed.getTemplateDataMap());
		System.out.println("# Feed Turtle\n" + feedTurtle);
	}

	@Test
	public void testFeedLevel() {
		assertEquals("checking feed title", "Feed Title", feed.getTitle());
		assertEquals("checking feed subtitle", "Feed subtitle", feed.getSubtitle());
		assertEquals("checking feed url", url, feed.getUrl());
		assertEquals("checking entry count", 2, feed.getEntries().size());
		assertEquals("checking HTML url", "http://www.example.com/main.html",
				feed.getHtmlUrl());
		assertEquals("checking webMaster", "john@gmail.com", feed.getAuthor()
				.getEmail());
		assertEquals("checking pubDate", "2009-09-06T16:20Z", feed
				.getDateStamp().getPublished());
		// TODO implement -note, canhave multiple links
		// assertEquals("checking feed link", , feed.getLink());
	}

	// webMaster, author, creator, guid, title,
	// "pubDate", , "link", "description"

	@Test
	public void testEntryLevel() {
		Entry entry0 = feed.getEntries().getEntry(0);

		assertEquals("checking entry title", "Entry 1", entry0.getTitle());
		assertEquals("checking entry author", "john@doe.com", entry0
				.getAuthor().getEmail());
		assertEquals("checking entry guid",
				"urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a", entry0.getId());
		assertEquals("checking entry content", "<p>Entry 1 content</p>",
				entry0.getContent());
		assertEquals("checking entry url", "http://example.org/entry1",
				entry0.getUrl());
		String published = entry0.getDateStamp().getPublished();
		// System.out.println(published);
		assertEquals("checking entry pubDate", "2009-09-07T16:20Z", published);
		Entry entry1 = feed.getEntries().getEntry(1);
		assertEquals("checking entry2 URL", "http://example.org/entry2",
				entry1.getUrl());
		assertEquals("checking entry creator", "JohnDoe", entry1.getAuthor()
				.getName());
		assertEquals("checking entry2 sort date", "2009-09-08T16:20Z",
				entry1.getDateStamp().getSortDate());

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
				// System.out.println("TRUE: " + link);
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

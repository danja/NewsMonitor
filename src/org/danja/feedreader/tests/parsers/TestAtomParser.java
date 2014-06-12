package org.danja.feedreader.tests.parsers;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Entry;
import org.danja.feedreader.model.EntryList;
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

public class TestAtomParser {

	private final String url = "http://localhost:8080/test-data/atom-sample.xml";
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
		feed.setFormatHint(FeedConstants.ATOM);
		// feed.setRefreshPeriod(Config.getPollerPeriod());

		// interpreter = RDFInterpreterFactory.createInterpreter(format);
		// feed.setInterpreter(interpreter);

		interpreter = InterpreterFactory.createInterpreter(feed);

		feed.setInterpreter(interpreter);
		feed.refresh();
		
		EntryList entries = feed.getEntries();
		
		// System.out.println(entries.getEntry(0).toTurtle());
		// System.out.println(feed.toTurtle());
		Templater.init();
		String feedTurtle = Templater.apply("feed-turtle-no-prefixes", feed.getTemplateDataMap());
		System.out.println("# Feed Turtle\n"+feedTurtle);
	}

	@Test
	public void testFeedLevel() {
		assertEquals("checking feed url", url, feed.getUrl());
		assertEquals("checking feed id",
				"urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6", feed.getId());
		assertEquals("checking feed title", "Feed Title", feed.getTitle());
		assertEquals("checking feed subtitle", "Feed subtitle", feed.getSubtitle());
		assertEquals("checking HTML url", "http://example.org/",
				feed.getHtmlUrl());
		assertEquals("checking entry count", 2, feed.getEntries().size());

		// TODO implement -note, canhave multiple links
		// assertEquals("checking feed link", , feed.getLink());
	}

	// (feed level) id, title, link, published, updated, author
	// * (entry level) as feed, plus content

	@Test
	public void testEntryLevel() {
		Entry entry0 = feed.getEntries().getEntry(0);
		assertEquals("checking entry1 URL", "http://example.org/entry1.html", entry0.getUrl());
		assertEquals("checking entry title", "Entry 1", entry0.getTitle());
		String normalSpaces = entry0.getContent().replaceAll("\\s+", " ");
		assertEquals("checking entry 1 content", "<div> <p>Entry 1 content</p> </div>", normalSpaces);
		assertEquals("checking entry 1 summary", "Entry 1 summary", entry0.getSummary());
		
		// <p>Entry 2 content <a href="http://example.com">with a link</a></p>
		Entry entry1 = feed.getEntries().getEntry(1);
		assertEquals("checking entry2 URL", "http://localhost:8080/entry2.html", entry1.getUrl());
		assertEquals("checking entry2 source date", "2014-05-08T18:30:02Z", entry1.getDateStamp().getSortDate());
		String normalSpaces1 = entry1.getContent().replaceAll("\\s+", " ");
		assertEquals("checking entry 2 content", "<div> <p>Entry 2 content <a href=\"http://example.com\">with a link</a></p> </div>", normalSpaces1);
		Set<Link> links = entry1.getLinks();
		boolean found = false;
		Iterator<Link> i = links.iterator();
		while (i.hasNext()) {
			Link link = i.next();
			// System.out.println("link: "+link);
			if ("http://example.com".equals(link.getHref())
					&& "with a link".equals(link.getLabel())) {
				found = true;
			}
		}
	  assertTrue("checking link in content", found);
	}

	@After
	public void tearDown() {
		
	}

	@AfterClass
	public static void stopServer() {
		// System.out.println("STOP");
		server.stop();
	}
}

package org.danja.feedreader.tests.parsers;

import static org.junit.Assert.*;

import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedImpl;
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.tests.utils.HttpServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAtomParser {

	private final String url = "http://localhost:8080/atom-sample.xml";
	private final static String rootDir = "data";
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
		feed.setRefreshPeriod(Config.getPollerPeriod());

		// interpreter = RDFInterpreterFactory.createInterpreter(format);
		// feed.setInterpreter(interpreter);

		interpreter = InterpreterFactory.createInterpreter(FeedConstants.ATOM);
		
		feed.setInterpreter(interpreter);
		feed.refresh();
	}

	@Test
	public void testFeedLevel() {
		assertEquals("checking feed title", "Feed Title", feed.getTitle());
		assertEquals("checking feed url", url, feed.getUrl());
		assertEquals("checking entry count", 2, feed.getEntries().size());
		
		// TODO implement -note, canhave multiple links
	//	assertEquals("checking feed link", , feed.getLink());
	}
	
	@Test
	public void testEntryLevel() {
		
	}

	
	@After
	public void tearDown() {
		
	}
	
	@AfterClass
	public static void stopServer() {
		server.stop();
	}
}

package it.danja.newsmonitor.tests.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import it.danja.newsmonitor.interpreters.Interpreter;
import it.danja.newsmonitor.interpreters.InterpreterFactory;
import it.danja.newsmonitor.model.Entry;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.FeedImpl;
import it.danja.newsmonitor.standalone.templating.FsTemplateLoader;
import it.danja.newsmonitor.templating.TemplateLoader;
import it.danja.newsmonitor.tests.utils.ConfigLoader;
import it.danja.newsmonitor.utils.ContentType;
import it.danja.newsmonitor.utils.HttpServer;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRss1Parser {

  private static Logger log = LoggerFactory.getLogger(TestRss1Parser.class);

  private final String url = "http://localhost:8088/test-data/rss1-sample.xml";
  private static final String rootDir =
    "src/main/resources/META-INF/resources/static/newsmonitor";
  private FeedImpl feed;
  private Interpreter interpreter;
  private static HttpServer server = new HttpServer(rootDir, 8088);

  @BeforeClass
  public static void startServer() {
    server.init();
    server.start();
  }

  @Before
  public void setUp() throws Exception {
    Properties config = ConfigLoader.getConfig();
    feed = new FeedImpl(config);
    feed.setUrl(url);
    feed.setFormatHint(ContentType.RSS1);
    interpreter = InterpreterFactory.createInterpreter(feed);
    feed.setInterpreter(interpreter);
    feed.refresh();
    TemplateLoader templater = new FsTemplateLoader(config);
    templater.init();
    //	String feedTurtle = FsTemplateLoader.apply("feed-turtle-no-prefixes", feed.getTemplateDataMap());
    //	log.info("# Feed Turtle\n"+feedTurtle);
  }

  @Test
  public void testFeedLevel() {
    assertEquals("checking feed title", "Feed Title", feed.getTitle());
    assertEquals("checking feed subtitle", "Feed subtitle", feed.getSubtitle());
    assertEquals(
      "checking feed id",
      "http://longoio.wordpress.com",
      feed.getId()
    );
    assertEquals("checking feed url", url, feed.getUrl());
    assertEquals("checking entry count", 2, feed.getEntries().size());
    assertEquals(
      "checking HTML url",
      "http://www.example.com/main.html",
      feed.getHtmlUrl()
    );
    assertEquals(
      "checking date",
      "2014-05-27T11:18:15Z",
      feed.getDateStamp().getUpdated()
    );
    // TODO implement -note, canhave multiple links
    // assertEquals("checking feed link", , feed.getLink());
  }

  // webMaster, author, creator, guid, title,
  // "pubDate", , "link", "description"
  @Test
  public void testEntryLevel() {
    Entry entry0 = feed.getEntries().getEntry(0);
    assertEquals(
      "checking entry id",
      "http://example.org/entry1",
      entry0.getId()
    );

    assertEquals("checking entry title", "Entry 1", entry0.getTitle());
    /*     assertEquals("checking entry 1 summary", "<p>Entry 1 summary</p>", entry0.getSummary());
        assertEquals("checking entry content", "<p>Entry 1 content</p>",
                entry0.getContent()); */
    assertEquals(
      "checking entry url",
      "http://example.org/entry1",
      entry0.getUrl()
    );
    String updated = entry0.getDateStamp().getUpdated();
    assertEquals("checking entry date", "2014-05-27T09:30:55Z", updated);

    Entry entry1 = feed.getEntries().getEntry(1);
    //	System.out.println("Entry1 = "+entry1);
    assertEquals(
      "checking entry2 URL",
      "http://example.org/entry2",
      entry1.getUrl()
    );
    assertEquals(
      "checking entry2 date",
      "2014-05-27T07:10:43Z",
      entry1.getDateStamp().getSortDate()
    );
    // <p>Entry 2 content <a href="http://example.com">with a link</a></p>
    Set<Link> links = entry1.getLinks();
    //		System.out.println("ENTRY1 : "+entry1);
    //		System.out.println("ENTRY1 content : "+entry1.getContent());
    //		System.out.println("LINKS "+links.size());
    boolean found = false;
    Iterator<Link> i = links.iterator();
    while (i.hasNext()) {
      Link link = i.next();
      //	log.info("link: " + link);
      if (
        "http://example.com".equals(link.getHref()) &&
        "with a link".equals(link.getLabel())
      ) {
        found = true;
        // log.info("TRUE: " + link);
      }
    }
    assertTrue("checking link in content", found);
  }

  @After
  public void tearDown() {}

  @AfterClass
  public static void stopServer() {
    server.stop();
  }
}

/**
 * NewsMonitor
 *
 * TestContentProcessor.java
 * @author danja
 * dc:date Aug 19, 2014
 *
 */
package it.danja.newsmonitor.tests.utils;

import static org.junit.Assert.assertEquals;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.utils.ContentProcessor;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 *
 */
public class TestContentProcessor {
	
	static final String ESCAPED_HTML = "&lt;p&gt;Entry content &lt;a href=\"http://example.com\"&gt;one&lt;/a&gt; or &lt;a href=\"http://example.com/2\"&gt;two&lt;/a&gt; link&lt;/a&gt;&lt;/p&gt;";
	static final String CDATA_HTML = "<![CDATA[<p>Entry content <a href=\"http://example.com\">one</a> or <a href=\"http://example.com/2\">two</a> link</a></p>]]>";
	
	static final String HTML = "<p>Entry content <a href=\"http://example.com\">one</a> or <a href=\"http://example.com/2\">two</a> link</a></p>";
	static final String ORIGIN = "http://origin";
	
	// TODO for some reason I wrote this test twice...these are bits from other version, check if there's anything useful
//	ContentProcessor processor = null;
//	String html = "<em>text</em>";
//	String cdata = "<![CDATA[<em>text</em>]]>";
//	String plain = "text";
//	String escaped = "&lt;em&gt;text&lt;/em&gt;";
	
	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#normaliseElement(java.lang.String)}.
	 */
	@Test
	public final void testNormaliseElement() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#stripTags(java.lang.String)}.
	 */
	@Test
	public final void testStripTags() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#unescape(java.lang.String)}.
	 */
	@Test
	public final void testUnescape() {
		assertEquals("checking escaped HTML", HTML, ContentProcessor.unescape(ESCAPED_HTML));
		assertEquals("checking CDATA", HTML, ContentProcessor.unescape(CDATA_HTML));
		
//		assertEquals("HTML input", html, ContentProcessor.unescape(html));
//		assertEquals("CDATA input", html, ContentProcessor.unescape(cdata));
//		assertEquals("plain input", plain, ContentProcessor.unescape(plain));
//		assertEquals("escaped input", html, ContentProcessor.unescape(escaped));
	}


	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#extractLinks(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testExtractLinks() {
		Set<Link> links = ContentProcessor.extractLinks(ORIGIN, HTML);
		assertEquals("checking link count", 2, links.size());
		
		Link link = null;
		Iterator<Link> iterator = links.iterator();
		while(iterator.hasNext()){
			link = iterator.next();
			if(link.getLabel().equals("two")) break;
		}
		assertEquals("checking link origin", "http://origin", link.getOrigin());
		assertEquals("checking link href", "http://example.com/2", link.getHref());

	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#extractHeadLinks(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testExtractHeadLinks() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#resolveUrl(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testResolveUrl() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#escapeQuotes(java.lang.String)}.
	 */
	@Test
	public final void testEscapeQuotes() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#stripPunctuation(java.lang.String)}.
	 */
	@Test
	public final void testStripPunctuation() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link it.danja.newsmonitor.utils.ContentProcessor#collapseSpaces(java.lang.String)}.
	 */
	@Test
	public final void testCollapseSpaces() {
		// fail("Not yet implemented"); // TODO
	}

}

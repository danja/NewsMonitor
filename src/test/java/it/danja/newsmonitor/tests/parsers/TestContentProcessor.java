package it.danja.newsmonitor.tests.parsers;


import static org.junit.Assert.assertEquals;
import it.danja.newsmonitor.utils.ContentProcessor;

import org.junit.Before;
import org.junit.Test;

public class TestContentProcessor {

	ContentProcessor processor = null;
	String html = "<em>text</em>";
	String cdata = "<![CDATA[<em>text</em>]]>";
	String plain = "text";
	String escaped = "&lt;em&gt;text&lt;/em&gt;";
	
	@Before
	public void setUp() throws Exception {
		// processor = new ContentProcessorImpl();
	}
	
	@Test
	public void testUnescape() {
		assertEquals("HTML input", html, ContentProcessor.unescape(html));
		assertEquals("CDATA input", html, ContentProcessor.unescape(cdata));
		assertEquals("plain input", plain, ContentProcessor.unescape(plain));
		assertEquals("escaped input", html, ContentProcessor.unescape(escaped));
	}

}

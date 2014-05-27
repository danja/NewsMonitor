/**
 * feedreader-prototype
 *
 * HtmlCleaner.java
 * @author danja
 * @date May 26, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.impl.LinkImpl;

/**
 *
 */
public class HtmlCleaner {

	private static final String[] excludeAttrs = { "font", "class" };

	public static final Set<String> excludeAttributes = new HashSet<String>();
	static {
		Collections.addAll(excludeAttributes, excludeAttrs);
	}

	public static String normaliseElement(String localName) {
		if ("b".equals(localName)) {
			return "strong";
		}
		if ("i".equals(localName)) {
			return "em";
		}
		return localName;
	}

	public static String stripTags(String raw) {
		if (raw == null || "".equals(raw))
			return "";
		String stripped = raw.replaceAll("<[^>]*>", " ");
		stripped.replaceAll("\\s+", " ").trim(); // collapse spaces
		return stripped;
	}

	public static String unescape(String raw) {
		if (raw == null || "".equals(raw))
			return "";
		return StringEscapeUtils.unescapeHtml4(raw);
	}

	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

	public static final Pattern A_PATTERN;
	public static final Pattern HREF_PATTERN;
	static {
		A_PATTERN = Pattern.compile(HTML_A_TAG_PATTERN);
		HREF_PATTERN = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}

	public static Set<Link> extractLinks(String content) {
		Set<Link> links = new HashSet<Link>();
		Matcher matcherTag = A_PATTERN.matcher(content);
		while (matcherTag.find()) {

			String hrefChunk = matcherTag.group(1); // href
			String linkText = matcherTag.group(2); // link text

			Matcher matcherLink = HREF_PATTERN.matcher(hrefChunk);

			while (matcherLink.find()) {

				String href = matcherLink.group(1);
				if(href.startsWith("\"") || href.startsWith("'")){
					href = href.substring(1, href.length()-1);
				}
				Link link = new LinkImpl();
				link.setHref(href);
				link.setLabel(linkText);

				links.add(link);

			}

		}
		return links;
	}
}

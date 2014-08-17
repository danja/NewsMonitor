/**
 * NewsMonitor
 *
 * ContentProcessor.java
 * @author danja
 * @date May 26, 2014
 *
 */
package it.danja.newsmonitor.utils;

import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.LinkImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 */
public class ContentProcessor {

	private static final String[] excludeAttrs = { "font", "class", "style" };

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
		// log.info("BEFORE "+raw);
		if (raw == null || "".equals(raw))
			return "";
		String stripped = raw.replaceAll("<[^>]*>", " ");

		// log.info("AFTER "+stripped);
		return stripped;
	}

	public static String unescape(String raw) {
		if (raw == null || "".equals(raw)) {
			return "";
		}
		if(raw.startsWith("<![CDATA[")) {
			return raw.substring(9, raw.length() -3);
		}
		return StringEscapeUtils.unescapeHtml4(raw);
	}

	private static final String A_REGEX = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HREF_REGEX = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	
	// <link rel="alternate" type="application/rss+xml" title="RSS" href="rss2-sample.xml">
	private static final String LINK_REGEX = "(?i)<link([^>]+)>";
	private static final String REL_REGEX = "\\s*(?i)rel\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	// private static final String TYPE_ATTR_PATTERN = "\\s*(?i)type\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

	public static final Pattern A_PATTERN;
	public static final Pattern LINK_PATTERN;
	public static final Pattern HREF_PATTERN;
	public static final Pattern REL_PATTERN;
	static {
		A_PATTERN = Pattern.compile(A_REGEX);
		LINK_PATTERN = Pattern.compile(LINK_REGEX);
		HREF_PATTERN = Pattern.compile(HREF_REGEX);
		REL_PATTERN = Pattern.compile(REL_REGEX);
	}
	
	public static Set<Link> extractLinks(Feed feed, String content) {
		return extractLinks(feed.getUrl(), content);
	}

	public static Set<Link> extractLinks(String origin, String content) {
		Set<Link> links = new HashSet<Link>();
		Matcher matcherTag = A_PATTERN.matcher(content);
		while (matcherTag.find()) {

			String hrefChunk = matcherTag.group(1); // href
			String linkText = matcherTag.group(2); // link text

			Matcher matcherLink = HREF_PATTERN.matcher(hrefChunk);

			while (matcherLink.find()) {

				String href = matcherLink.group(1);
				if (href.startsWith("\"") || href.startsWith("'")) {
					href = href.substring(1, href.length() - 1);
				}
				href = href.trim();
//				try {
//					URI uri = new URI(href); // ????
//				} catch (URISyntaxException e) {
//					continue;
//				}
				try {
					href = resolveUrl(origin, href);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Link link = new LinkImpl();
				link.setOrigin(origin);
				link.setHref(href);
				link.setLabel(linkText);
				links.add(link);
			}
		}
		return links;
	}
	
	public static Set<Link> extractHeadLinks(String origin, String content) {
		Set<Link> links = new HashSet<Link>();
		Matcher matcherTag = LINK_PATTERN.matcher(content);
		while (matcherTag.find()) {

			String attributesChunk = matcherTag.group(1); // href
	//		String linkText = matcherTag.group(2); // link text

			Matcher hrefMatcher = HREF_PATTERN.matcher(attributesChunk);
			Matcher relMatcher = REL_PATTERN.matcher(attributesChunk);

			if (hrefMatcher.find()) {

				String href = hrefMatcher.group(1);
				if (href.startsWith("\"") || href.startsWith("'")) {
					href = href.substring(1, href.length() - 1);
				}
				href = href.trim();
//				try {
//					URI uri = new URI(href);
//				} catch (URISyntaxException e) {
//					continue;
//				}
				try {
					href = resolveUrl(origin, href);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Link link = new LinkImpl();
				link.setOrigin(origin);
				link.setHref(href);
			//	link.setLabel(linkText);
				if (relMatcher.find()) {
					String rel = relMatcher.group(1);
					if (rel.startsWith("\"") || rel.startsWith("'")) {
						rel = rel.substring(1, rel.length() - 1);
					}
					rel = rel.trim();
				//	log.info("REL = "+rel);
					link.setRel(rel);
				}
				links.add(link);
			}
		}
		return links;
	}

	public static String resolveUrl(String url, String href)
			throws URISyntaxException {
		if (href == null || url == null) { // dud
			return null;
		}
		if (href.startsWith("http://") || href.startsWith("https://")) { // already
																			// absolute
			return href;
		}
		URI uri = new URI(url);

		// log.info("URI = "+uri);
		// log.info("HREF = "+href);
		URI hrefUri = new URI(href);

		return uri.resolve(hrefUri).toString();
	}

	public static String escapeQuotes(String content) {
		if (content == null || "".equals(content)) {
			return "";
		}
		return content.replaceAll("\"", "\\\\\"");
	}

	public static String stripPunctuation(String content) { // is Unicode-aware
															// version
		return content.replaceAll("\\p{P}", " ");
	}

	public static String collapseSpaces(String content) {
		return content.replaceAll("\\s+", " ").trim(); 
	}


}

/**
 * feedreader-prototype
 *
 * SparqlTemplater.java
 * @author danja
 * @date May 28, 2014
 *
 */
package org.danja.newsmonitor.sparql;

import java.util.HashMap;
import java.util.Map;

import org.danja.newsmonitor.io.HttpMessage;
import org.danja.newsmonitor.io.SparqlConnector;
import org.danja.newsmonitor.io.TextFileReader;
import org.danja.newsmonitor.main.Config;
import org.danja.newsmonitor.model.Feed;
import org.danja.newsmonitor.templating.Templater;

/**
 *
 */
public class SparqlTemplater {

	private static final String PREFIXES;
	
	static {
		PREFIXES = TextFileReader.read(Config.SPARQL_PREFIXES_FILENAME);
	}
	
	public static void main(String[] args) {
		System.out.println(PREFIXES);
	}
	
	public static HttpMessage uploadFeed(Feed feed) {
		String feedBody = Templater.apply("feed-turtle-no-prefixes", feed.getTemplateDataMap());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prefixes", PREFIXES);
		map.put("feedUrl", feed.getUrl());
		map.put("body", feedBody);
		
		String sparql = Templater.apply("sparql-insert", map);
		// System.out.println("\n\n----------------\n"+sparql+"\n\n---------------------");
		HttpMessage message = SparqlConnector.update(Config.UPDATE_ENDPOINT, sparql);
		message.setRequestBody(sparql);
		return message;
	}
}

/**
 * NewsMonitor
 *
 * SparqlTemplater.java
 * @author danja
 * @date May 28, 2014
 *
 */
package it.danja.newsmonitor.sparql;

import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.templating.Templater;

import java.util.HashMap;
import java.util.Map;

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

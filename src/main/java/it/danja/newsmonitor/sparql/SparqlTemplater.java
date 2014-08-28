/**
 * NewsMonitor
 *
 * SparqlTemplater.java
 * @author danja
 * @date May 28, 2014
 *
 */
package it.danja.newsmonitor.sparql;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.standalone.FsTextFileReader;
import it.danja.newsmonitor.standalone.templating.FsTemplateLoader;
import it.danja.newsmonitor.templating.TemplateLoader;
import it.danja.newsmonitor.templating.Templater;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class SparqlTemplater {
	
	private static Logger log = LoggerFactory.getLogger(SparqlTemplater.class);

	private String PREFIXES;
        
            private SparqlConnector sparqlConnector = new SparqlConnector();

			private Templater templater = null;
	
	public SparqlTemplater(Properties config, TextFileReader textFileReader, Templater templater) {
		this.templater  = templater;
		// PREFIXES = TextFileReader.read(Config.SPARQL_PREFIXES_FILE);
			PREFIXES = textFileReader.read("SPARQL_PREFIXES_LOCATION");
	}
	
//private BundleContext bundleContext;
//	
//	public void setBundleContext(BundleContext bundleContext) {
//		this.bundleContext = bundleContext;
//	}
	
//	public static void main(String[] args) {
//		log.info(PREFIXES);
//	}
	
	public  HttpMessage uploadFeed(Feed feed) {
		String feedBody = templater.apply("feed-turtle-no-prefixes", feed.getTemplateDataMap());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prefixes", PREFIXES);
		map.put("feedUrl", feed.getUrl());
		map.put("body", feedBody);
		
		String sparql = templater.apply("sparql-insert", map);
		// log.info("\n\n----------------\n"+sparql+"\n\n---------------------");
		HttpMessage message = sparqlConnector.update(Config.UPDATE_ENDPOINT, sparql);
		message.setRequestBody(sparql);
		return message;
	}
}

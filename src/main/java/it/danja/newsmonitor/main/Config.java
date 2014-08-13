/**
 * NewsMonitor
 *
 * Config.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.discovery.PresetTopics;
import it.danja.newsmonitor.discovery.Topic;

/**
 *
 */
public class Config {
	public static final long TEST_RUN = 0; // stop after this no. minutes, 0 =
	// infinite
	public static final Topic TOPIC = PresetTopics.WOODCARVING;
	
	/* File Resources */
	// buggy-list.txt rdf-bloggers-feedlist.txt; short-list.txt woodcarvers.txt
	public static final String SEED_FEEDLIST = "src/main/resources/feedlists/short-list.txt";
	
	public static final String BOOTSTRAP_SPARQL = "src/main/resources/sparql/bootstrap.sparql";

	public static final String SPARQL_FEEDLIST_FILENAME = "src/main/resources/sparql/get-feedlist.sparql";

	public static final String TEMPLATES_DIR = "src/main/resources/templates";

	public static final String STOPWORDS_FILENAME = "src/main/resources/misc/stopwords.txt";

	public static final String SPARQL_GET_STATUS = "src/main/resources/sparql/get-system-status.sparql";

	public static final String GET_LINKS_SPARQL = "src/main/resources/sparql/get-new-links.sparql";
	public static final String SPARQL_PREFIXES_FILENAME = "src/main/resources/sparql/prefixes.sparql";
	
	/* URLs */
	public static final String QUERY_ENDPOINT = "http://localhost:3030/feedreader/query";
	public static final String UPDATE_ENDPOINT = "http://localhost:3030/feedreader/update";

	public static final float SUBSCRIBE_RELEVANCE_THRESHOLD = 2F;
	
	public static final float UNSUBSCRIBE_RELEVANCE_THRESHOLD = 0.5F;
	
	/**
	 * Pause between polling runs
	 */
	public static int REFRESH_PERIOD = 100; // milliseconds

	/**
	 * Pause between reading each feed
	 */
	public static int PER_FEED_SLEEP_PERIOD = 100;
	public static final int CONNECT_TIMEOUT = 1000; // milliseconds
	public static final int READ_TIMEOUT = 2000; // milliseconds
	public static final int LINK_EXPLORER_SLEEP_PERIOD = 500; // per-link

	/**
	 * Number of attempts to read a feed which is failing before flagging it as
	 * dead
	 */
	public static final int MAX_LIVES = 3;
	
	public static final String FEED_USER_AGENT_HEADER = "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.97 Safari/537.36 NewsMonitor/1.0.0";

	public static final String HTML_USER_AGENT_HEADER = "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.97 Safari/537.36 NewsMonitor/1.0.0";
	
	public static final String FEED_ACCEPT_HEADER = "application/rdf+xml, application/atom+xml, application/rss+xml, application/xml, text/xml";

	public static final String HTML_ACCEPT_HEADER = "text/html, application/xhtml+xml, application/xml";


}
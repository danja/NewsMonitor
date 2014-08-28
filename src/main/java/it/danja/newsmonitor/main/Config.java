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
 * TODO move this to an external declarative file (RDF, properties or similar)
 */
public class Config {
	
	public static final String USERNAME = "admin"; // Stanbol defaults
	public static final String PASSWORD = "admin";

	public static final char STANDALONE_BUILD = 0;
	public static final char OSGI_BUILD = 1;

	// this is flipped to STANDALONE_BUILD if NewsMonitor is run via Main.main
	public static char BUILD_TYPE = OSGI_BUILD;

	public static final long TEST_RUN = 0; // stop after this no. minutes, 0 =
	// infinite
	public static final Topic TOPIC = PresetTopics.WOODCARVING;

	/* File Resources */
	// buggy-list.txt rdf-bloggers-feedlist.txt; short-list.txt woodcarvers.txt
	public static final String SEED_FEEDLIST_FILE = "src/main/resources/feedlists/short-list.txt";
	public static final String SEED_FEEDLIST_IN_BUNDLE = "feedlists/short-list.txt";

	// public static final String BOOTSTRAP_SPARQL_FILE =
	// "src/main/resources/sparql/bootstrap.sparql";
	// public static final String BOOTSTRAP_SPARQL_IN_BUNDLE =
	// "sparql/bootstrap.sparql";



	public static final float SUBSCRIBE_RELEVANCE_THRESHOLD = 2F;

	public static final float UNSUBSCRIBE_RELEVANCE_THRESHOLD = 0.5F;

	/**
	 * Pause between polling runs
	 */
	public static int REFRESH_PERIOD = 60 * 60 * 1000; // milliseconds

	/**
	 * Pause between reading each feed
	 */
	public static int PER_FEED_SLEEP_PERIOD = 1000; // milliseconds
        
	public static final int LINK_EXPLORER_SLEEP_PERIOD = 60 * 60 * 1000; // per-link

        	public static final int CONNECT_TIMEOUT = 1000; // milliseconds
	public static final int READ_TIMEOUT = 2000; // milliseconds
        
	/**
	 * Number of attempts to read a feed which is failing before flagging it as
	 * dead
	 */
	public static final int MAX_LIVES = 3;

	public static final String FEED_USER_AGENT_HEADER = "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.97 Safari/537.36 NewsMonitor/1.0.0";

	public static final String HTML_USER_AGENT_HEADER = "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.97 Safari/537.36 NewsMonitor/1.0.0";

	public static final String FEED_ACCEPT_HEADER = "application/rdf+xml, application/atom+xml, application/rss+xml, application/xml, text/xml";

	public static final String HTML_ACCEPT_HEADER = "text/html, application/xhtml+xml, application/xml";
	public static final boolean POLLER_NO_LOOP = false; // for debugging
	
	public static final String SPARQL_FEEDLIST_FILE = "src/main/resources/sparql/get-feedlist.sparql";
	public static final String SPARQL_FEEDLIST_IN_BUNDLE = "sparql/get-feedlist.sparql";

	public static final String TEMPLATES_DIR = "src/main/resources/templates/html/it/danja/newsmonitor/resource";
	public static final String TEMPLATES_DIR_IN_BUNDLE = "templates/html/it/danja/newsmonitor/resource";

	// public static final String STOPWORDS_FILENAME =
	// "src/main/resources/misc/stopwords.txt";

	public static final String SPARQL_GET_STATUS_FILE = "src/main/resources/sparql/get-system-status.sparql";
	public static final String SPARQL_GET_STATUS_IN_BUNDLE = "sparql/get-system-status.sparql";

	public static final String GET_LINKS_SPARQL_FILE = "src/main/resources/sparql/get-new-links.sparql";
	public static final String GET_LINKS_SPARQL_IN_BUNDLE = "sparql/get-new-links.sparql";

	public static final String SPARQL_PREFIXES_FILE = "src/main/resources/sparql/prefixes.sparql";
	public static final String SPARQL_PREFIXES_IN_BUNDLE = "sparql/prefixes.sparql";

	/* URLs */
	public static final String SPARQL_SCHEME = "http";
	public static final String SPARQL_HOST = "localhost";
	public static final int SPARQL_PORT = 8080;
	public static final String SPARQL_QUERY_PATH = "/sparql";
	public static final String SPARQL_UPDATE_PATH = "/update";
	public static final String SPARQL_URL_BASE = SPARQL_SCHEME+"://"+SPARQL_HOST+":"+SPARQL_PORT;
	
	// "http://localhost:8080/sparql"; , "http://localhost:8080/update";
	public static final String QUERY_ENDPOINT = SPARQL_URL_BASE+SPARQL_QUERY_PATH;
	public static final String UPDATE_ENDPOINT = SPARQL_URL_BASE+SPARQL_UPDATE_PATH;

	public static final String CONFIG_PROPERTIES_BUNDLE_LOCATION = "bundle-config.properties";
	public static final String CONFIG_PROPERTIES_STANDALONE_LOCATION = "standalone-config.properties";	
	// public static final String QUERY_ENDPOINT = "http://localhost:3030/feedreader/query";
	// public static final String UPDATE_ENDPOINT = "http://localhost:3030/feedreader/update";

}

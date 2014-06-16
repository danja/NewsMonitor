/**
 * feedreader-prototype
 *
 * Config.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package org.danja.feedreader.main;

/**
 *
 */
public class Config {

	// buggy-list.txt rdf-bloggers-feedlist.txt; input/short-list.txt
	public static final String SEED_FEEDLIST = "input/short-list.txt";
	// public static String QUERY_ENDPOINT =
	// "http://localhost:3030/feedreader/query";

	public static final long TEST_RUN = 0; // stop after this no. minutes, 0 =
											// infinite

	public static final String BOOTSTRAP_SPARQL = "sparql/bootstrap.sparql";

	public static final String SPARQL_FEEDLIST_FILENAME = "sparql/get-feedlist.sparql";

	public static final String QUERY_ENDPOINT = "http://localhost:3030/feedreader/query";
	public static final String UPDATE_ENDPOINT = "http://localhost:3030/feedreader/update";

	public static final String TEMPLATES_DIR = "templates";

	public static final String SPARQL_PREFIXES_FILENAME = "sparql/prefixes.sparql";

	public static final int CONNECT_TIMEOUT = 5000; // milliseconds
	public static final int READ_TIMEOUT = 5000; // milliseconds

	/**
	 * Number of attempts to read a feed which is failing before flagging it as
	 * dead
	 */
	public static final int MAX_LIVES = 3;

	public static final String STOPWORDS_FILENAME = "input/stopwords.txt";

	public static final int LINK_EXPLORER_SLEEP_PERIOD = 1000;

	public static final String SPARQL_GET_STATUS = "sparql/get-system-status.sparql";

	public static final String GET_LINKS_SPARQL = "sparql/get-new-links.sparql";

	/**
	 * Pause between polling runs
	 */
	public static int REFRESH_PERIOD = 500; // milliseconds

	/**
	 * Pause between reading each feed
	 */
	public static int PER_FEED_SLEEP_PERIOD = 500;

	private static int MAX_ITEMS = 10;

	// public static void load() {
	// // TODO read from store
	// }

	// public static int getMaxItems() {
	// return MAX_ITEMS;
	// }

}

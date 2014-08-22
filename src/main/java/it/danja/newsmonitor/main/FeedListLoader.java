/**
 * NewsMonitor
 *
 * FeedListLoader.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.main;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.io.OpmlSetReader;
import it.danja.newsmonitor.io.SparqlConnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Read text list from disk, wrap with SPARQL template, push into store
 */
public class FeedListLoader {
	
	private static Logger log = LoggerFactory.getLogger(FeedListLoader.class);

	// OpmlSetReader reader = new OpmlSetReader();
	// return reader.load(filename);
	
	private static int urlCount = 0;

	public static String SPARQL_TEMPLATE = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX rss: <http://purl.org/rss/1.0/> \n"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
			+ "INSERT DATA {\n ${channels} \n}";

	// TODO move to Config
	public static String CHANNEL_TEMPLATE = "<${url}> rdf:type rss:channel ; \n"
			+ "foaf:topic <http://www.w3.org/2001/sw/>, <http://www.w3.org/RDF/> . \n\n";

	private BundleContext bundleContext;
	
	public FeedListLoader(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		// log.debug(bundleContext.toString());
	}

	public static void main(String[] args) {

//		FeedListLoader loader = new FeedListLoader();
//		LineHandler handler = loader.new LineHandler();
//		String turtleBody = loader.readFile("input/rdf-bloggers-feedlist.txt",
//				handler);
//		String sparql = FeedListLoader.insertValue(SPARQL_TEMPLATE, "channels",
//				turtleBody);
//		// log.info("Query = \n" + sparql);
//		int responseCode = SparqlConnector.update(
//				"http://localhost:3030/feedreader/update", sparql).getStatusCode();
		// log.info(responseCode);
	}

	/**
	 * Reads text file from disk line by line
	 * 
	 * @param filename
	 *            name of target file
	 * @return String containing text content
	 */
	public String readFile() {
		log.debug(bundleContext.toString());
		LineHandler handler = new LineHandler();
		File file = null;
		BufferedReader reader = null;
		if (Config.BUILD_TYPE == Config.STANDALONE_BUILD) {
			file = new File(Config.SEED_FEEDLIST_FILE);
			try {
				reader = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
			}
		} else {
			
			Bundle bundle = bundleContext.getBundle();
			URL url = bundle.getEntry(Config.SEED_FEEDLIST_IN_BUNDLE);
			StringBuffer buffer = null;
			// if (url != null) {
			//InputStream inputStream = null;
			if(url == null) {
				throw new RuntimeException("Null URL from path  "+Config.SEED_FEEDLIST_IN_BUNDLE);
			}
			try {
				InputStream  inputStream = url.openStream();

			//	String string = "";
			//	buffer = new StringBuffer();

				reader = new BufferedReader(
						new InputStreamReader(inputStream));
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}
		}
		
		

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				handler.handle(line);
			}
			reader.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		log.info(urlCount+" URLs loaded from file");
		
		return handler.getTurtleBody();
	}

	/**
	 * Ultra-crude templating
	 * s
	 * @param template
	 * @param url
	 * @return
	 */
	public static String insertValue(String template, String name, String value) {
		return template.replace("${" + name + "}", value);
	}

	class LineHandler {
		StringBuffer targetBuffer = new StringBuffer();

		public void handle(String line) {
			if (line.trim().length() != 0 && line.charAt(0) != '#') { // allow comments and blank lines
				String sparql = FeedListLoader.insertValue(CHANNEL_TEMPLATE,
						"url", line.trim());
				targetBuffer.append(sparql);
				urlCount++;
			}
		}

		public String getTurtleBody() {
			return targetBuffer.toString();
		}
	}

}

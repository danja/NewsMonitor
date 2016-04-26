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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.io.TextFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

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
			+ "foaf:topic <https://en.wikipedia.org/wiki/Deep_learning> . \n\n";

	private Properties config = null;

	private TextFileReader textFileReader = null;
	
	public FeedListLoader(Properties config, TextFileReader textFileReader) {
		this.config = config;
		this.textFileReader  = textFileReader;
	}


	/**
	 * Reads text file from disk line by line
	 * 
	 * TODO switch add hoc templating to freemarker
	 * 
	 * @param filename
	 *            name of target file
	 * @return String containing text content
	 */
	public String readFile(String location) {
		
		BufferedReader reader = new BufferedReader(textFileReader.getReader(location));
		LineHandler handler = new LineHandler();

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
		// System.out.println("handler.getTurtleBody() "+handler.getTurtleBody());
		return handler.getTurtleBody();
	}

	/**
	 * TODO ready to remove (using freemarker instead)
	 * Ultra-crude templating
	 * 
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

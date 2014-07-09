/**
 * feedreader-prototype
 *
 * FeedListLoader.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.newsmonitor.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.danja.newsmonitor.io.OpmlSetReader;
import org.danja.newsmonitor.io.SparqlConnector;

/**
 * Read text list from disk, wrap with SPARQL template, push into store
 */
public class FeedListLoader {

	// OpmlSetReader reader = new OpmlSetReader();
	// return reader.load(filename);
	
	private static int urlCount = 0;

	public static String SPARQL_TEMPLATE = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX rss: <http://purl.org/rss/1.0/> \n"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
			+ "INSERT DATA {\n ${channels} \n}";

	public static String CHANNEL_TEMPLATE = "<${url}> rdf:type rss:channel ; \n"
			+ "foaf:topic <http://www.w3.org/2001/sw/>, <http://www.w3.org/RDF/> . \n\n";

	public static void main(String[] args) {

		FeedListLoader loader = new FeedListLoader();
		LineHandler handler = loader.new LineHandler();
		String turtleBody = loader.readFile("input/rdf-bloggers-feedlist.txt",
				handler);
		String sparql = FeedListLoader.insertValue(SPARQL_TEMPLATE, "channels",
				turtleBody);
		// System.out.println("Query = \n" + sparql);
		int responseCode = SparqlConnector.update(
				"http://localhost:3030/feedreader/update", sparql).getStatusCode();
		// System.out.println(responseCode);
	}

	/**
	 * Reads text file from disk line by line
	 * 
	 * @param filename
	 *            name of target file
	 * @return String containing text content
	 */
	public static String readFile(String filename, LineHandler handler) {
		File file = new File(filename);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				handler.handle(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(urlCount+" URLs loaded from file");
		
		return handler.getTurtleBody();
	}

	/**
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

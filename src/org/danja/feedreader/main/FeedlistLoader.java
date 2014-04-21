package org.danja.feedreader.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.danja.feedreader.io.OpmlSetReader;
import org.danja.feedreader.io.SparqlConnector;

/**
 * Read text list from disk, wrap with SPARQL template, push into store
 */
public class FeedlistLoader {
	
//	OpmlSetReader reader = new OpmlSetReader();
//    return reader.load(filename);

	public static String SPARQL_TEMPLATE = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX rss: <http://purl.org/rss/1.0/> \n"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
			+ "INSERT DATA {\n ${channels} \n}";

	public static String CHANNEL_TEMPLATE = "<${url}> rdf:type rss:channel ; \n"
			+ "foaf:topic <http://www.w3.org/2001/sw/>, <http://www.w3.org/RDF/> . \n\n";

	public static void main(String[] args) {

		FeedlistLoader loader = new FeedlistLoader();
		LineHandler handler = loader.new LineHandler();
		String turtleBody = loader.readFile("input/rdf-bloggers-feedlist.txt",
				handler);
		String sparql = FeedlistLoader.insertValue(SPARQL_TEMPLATE, "channels",
				turtleBody);
		System.out.println("Query = \n" + sparql);
		int responseCode = SparqlConnector.update(
				"http://localhost:3030/feedreader/update", sparql);
		System.out.println(responseCode);
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
			String sparql = FeedlistLoader.insertValue(CHANNEL_TEMPLATE, "url",
					line);
			targetBuffer.append(sparql);
		}

		public String getTurtleBody() {
			return targetBuffer.toString();
		}
	}

}

/**
 * feedreader-prototype
 *
 * Main.java
 * @author danja
 * @date Apr 23, 2014
 *
 */
package org.danja.feedreader.main;

import java.util.List;

import org.danja.feedreader.io.FeedList;
import org.danja.feedreader.io.SparqlConnector;
import org.danja.feedreader.main.FeedlistLoader.LineHandler;

/**
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.loadSeedFeedList();
		FeedList feedList = new FeedList();
		feedList.load();
		System.out.println(feedList);

	}
	
	public void loadSeedFeedList() {
		FeedlistLoader loader = new FeedlistLoader();
		LineHandler handler = loader.new LineHandler();
		String turtleBody = loader.readFile("input/rdf-bloggers-feedlist.txt",
				handler);
		String sparql = FeedlistLoader.insertValue(FeedlistLoader.SPARQL_TEMPLATE, "channels",
				turtleBody);
		// System.out.println("Query = \n" + sparql);
		int responseCode = SparqlConnector.update(
				"http://localhost:3030/feedreader/update", sparql);
		// System.out.println(responseCode);
	}

}

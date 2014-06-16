/**
 * feedreader-prototype
 *
 * LinkExplorer.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.feedreader.discovery;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.danja.feedreader.interpreters.HtmlHandler;
import org.danja.feedreader.interpreters.SoupParser;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.SparqlConnector;
import org.danja.feedreader.io.TextFileReader;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.FeedList;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Page;
import org.danja.feedreader.model.impl.PageImpl;
import org.danja.feedreader.sparql.SparqlResultsParser;
import org.danja.feedreader.sparql.SparqlResults.Result;
import org.danja.feedreader.utils.HtmlCleaner;

/**
 *
 */
public class LinkExplorer implements Runnable {

	private FeedList feedList = null;
	private boolean running = false;
	private Thread thread = null;
	private boolean stopped = false;
	private Link link;
	private HttpConnector connector = new HttpConnector();
	private String url;

	/**
	 * 
	 */
	public LinkExplorer(FeedList feedList) {
		this.feedList = feedList;
	}

	public void start() {
		running = true;
		stopped = false;
		thread = new Thread(this);
		thread.start();

	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		while (running) {
			// Set<Link> links = feedList.getAllLinks();
			Set<Link> links = getLinksFromStore();
			Iterator<Link> linkIterator = links.iterator();
			while (linkIterator.hasNext()) {
				Link link = linkIterator.next();
				explore(link);
				updateStore(link);
			}
			try {
				Thread.sleep(Config.LINK_EXPLORER_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//updateStore();
		}
		stopped = true;
	}

	private void updateStore(Link link) {
		// TODO Auto-generated method stub
		
	}

	private Set<Link> getLinksFromStore() {
		String sparql = TextFileReader.read(Config.GET_LINKS_SPARQL);
		String xmlResults = SparqlConnector.query(Config.QUERY_ENDPOINT, sparql);
		
		System.out.println("XMLRESULTS = "+xmlResults);
		
		SparqlResultsParser parser = new SparqlResultsParser();
		List<Result> results = parser.parse(xmlResults).getResults();
		Set<Link> links = new HashSet<Link>();
		for (int i = 0; i < results.size(); i++) {
			 System.out.println(results.get(i));
			// String link = results.get(i).iterator().next().getValue();
			// feeds.add(feed);
		}
		return null;
	}

	private void explore(Link link) {
		this.link = link;
		this.url = link.getHref();
		
		if(!isRecognised()) { // no use
			link.setRelevance(0F);
			link.setExplored(true);
			return;
		}
		String data = connector.downloadAsString(url);
		if(data != null) {
			RelevanceCalculator relevanceCalculator = new RelevanceCalculator();
			float relevance = relevanceCalculator.calculateRelevance(PresetTopics.SEMWEB_TOPIC, data); 
			link.setRelevance(relevance);
		}
		link.setExplored(true);
	}

	private boolean isRecognised() {
		if(ContentType.identifyExtension(url) != 0) {
			return true;
		}
		if(ContentType.identifyContentType(connector.getContentType()) != 0) {
			return true;
		}
		return false;
	}

		

	public boolean isStopped() {
		return stopped;
	}

}

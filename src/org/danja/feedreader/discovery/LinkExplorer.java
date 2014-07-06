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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.danja.feedreader.interpreters.HtmlHandler;
import org.danja.feedreader.interpreters.SoupParser;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.HttpMessage;
import org.danja.feedreader.io.SparqlConnector;
import org.danja.feedreader.io.TextFileReader;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.FeedList;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Page;
import org.danja.feedreader.model.impl.LinkImpl;
import org.danja.feedreader.model.impl.PageImpl;
import org.danja.feedreader.sparql.SparqlResults.Binding;
import org.danja.feedreader.sparql.SparqlResultsParser;
import org.danja.feedreader.sparql.SparqlResults.Result;
import org.danja.feedreader.templating.Templater;
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
				if (link.isExplored() || link.getResponseCode() >= 400) {
					continue;
				}
				System.out.println("LINK : " + link);
				explore(link);
				link.setExplored(true);
				
				updateStore(link);
			}
			try {
				Thread.sleep(Config.LINK_EXPLORER_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stopped = true;
	}

	

	private void explore(Link link) {
		if(!link.getHref().startsWith("http://") && !link.getHref().startsWith("https://")) return;
		System.out.println("Exploring " + link.getHref() + "...");
		this.link = link;
		this.url = link.getHref();
		connector.setUrl(url);

		// as "+ContentType.formatName(link.))
		String data = connector.downloadAsString(url);
		
		
		
		int responseCode = connector.getResponseCode();
		link.setResponseCode(responseCode);

		//System.out.println("DATA = \n"+data);
		if (data != null && responseCode == 200) {
			String contentType = connector.getContentType();
			char format = ContentType.identifyFormat(contentType);
			System.out.println("Link " + link.getHref() + " recognised as "
					+ ContentType.formatName(format));
			link.setFormat(ContentType.formatName(format));
			
//			System.out.println("Link " + link.getHref() + " recognised as "
//					+ ContentType.formatName(type));
			
			if (format == ContentType.UNKNOWN) { // no use
				link.setRelevance(0F);
				return;
			}
//			String contentType = connector.getContentType();
//			link.setContentType(contentType);
//			link.setFormat(ContentType.formatName(ContentType.identifyContentType(contentType)));
			
			RelevanceCalculator relevanceCalculator = new RelevanceCalculator();
			float relevance = relevanceCalculator.calculateRelevance(
					PresetTopics.SEMWEB_TOPIC, data);
			link.setRelevance(relevance);
			System.out.println("EXPLORED " + link);
		} else {
			link.setContentType(null);
			link.setFormat(ContentType.formatName(ContentType.UNKNOWN));
			link.setRelevance(0);
			link.setRelevance(0F);
		}

	}

//	private char identifyType() {
//		char type = ContentType.identifyExtension(url);
//		if (type != 0F) {
//			return type;
//		}
//		return ContentType.identifyContentType(connector.getContentType());
//	}

	public boolean isStopped() {
		return stopped;
	}

	private void updateStore(Link link) {
		System.out.println("Updating link " + link.getHref() + " to store...");
		String sparql = Templater.apply("update-links",
				link.getTemplateDataMap());
		System.out.println("\n\n----------------\n"+sparql+"\n\n---------------------");
		HttpMessage message = SparqlConnector.update(Config.UPDATE_ENDPOINT,
				sparql);
		message.setRequestBody(sparql);
		System.out.println("updated link status : " + message.getStatusCode()
				+ " " + message.getStatusMessage());
		if (message.getStatusCode() >= 400) {
			System.out.println(message);
			System.out.println("SPARQL = \n" + message.getRequestBody());
		}
	}

	private Set<Link> getLinksFromStore() {
		String sparql = TextFileReader.read(Config.GET_LINKS_SPARQL);
		String xmlResults = SparqlConnector
				.query(Config.QUERY_ENDPOINT, sparql);

		// System.out.println("XMLRESULTS = "+xmlResults);

		SparqlResultsParser parser = new SparqlResultsParser();
		List<Result> results = parser.parse(xmlResults).getResults();
		Set<Link> links = new HashSet<Link>();
		for (int i = 0; i < results.size(); i++) {
			// System.out.println(results.get(i));

			Result result = results.get(i);
			// .iterator().next().getName();

			Link link = new LinkImpl();
			Iterator<Binding> iterator = result.iterator();

			while (iterator.hasNext()) {
				Binding binding = iterator.next();
				String name = binding.getName();
				String value = binding.getValue();
				setValue(link, name, value);
				// System.out.println("\n"+name+" = "+value);

			}
			links.add(link);
		}
		return links;
	}

	private void setValue(Link link, String name, String value) {
		if ("origin".equals(name)) {
			link.setOrigin(value);
		}
		if ("href".equals(name)) {
			link.setHref(value);
		}
		if ("label".equals(name)) {
			link.setLabel(value);
		}
		if ("rel".equals(name)) {
			link.setRel(value);
		}
		if ("responseCode".equals(name)) {
			link.setResponseCode(Integer.parseInt(value));
		}
		if ("contentType".equals(name)) {
			link.setContentType(value);
		}
		if ("format".equals(name)) {
			link.setFormat(value);
		}
		if ("explored".equals(name)) {
			System.out.println("EXPLORED = "+value);
			link.setExplored("true".equals(value));
		}
		if ("remote".equals(name)) {
			link.setRemote("true".equals(value));
		}
		if ("relevance".equals(name)) {
			link.setRelevance(value.charAt(0));
		}
	}
	
}

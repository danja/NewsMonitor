/**
 * feedreader-prototype
 *
 * LinkExplorer.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.newsmonitor.discovery;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.danja.newsmonitor.interpreters.HtmlHandler;
import org.danja.newsmonitor.interpreters.SoupParser;
import org.danja.newsmonitor.io.HttpConnector;
import org.danja.newsmonitor.io.HttpMessage;
import org.danja.newsmonitor.io.SparqlConnector;
import org.danja.newsmonitor.io.TextFileReader;
import org.danja.newsmonitor.main.Config;
import org.danja.newsmonitor.model.Feed;
import org.danja.newsmonitor.model.FeedList;
import org.danja.newsmonitor.model.Link;
import org.danja.newsmonitor.model.Page;
import org.danja.newsmonitor.model.impl.LinkImpl;
import org.danja.newsmonitor.model.impl.PageImpl;
import org.danja.newsmonitor.sparql.SparqlResultsParser;
import org.danja.newsmonitor.sparql.SparqlResults.Binding;
import org.danja.newsmonitor.sparql.SparqlResults.Result;
import org.danja.newsmonitor.templating.Templater;
import org.danja.newsmonitor.utils.ContentProcessor;
import org.danja.newsmonitor.utils.ContentType;
import org.danja.newsmonitor.utils.HtmlCleaner;

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
				if (feedList.containsFeed(link.getHref())) {
					link.setExplored(true);
					continue;
				}
				if (link.isExplored()) {
					continue;
				}
				// System.out.println("LINK : " + link);
				explore(link);
				link.setExplored(true);

				updateStore(link);
				try {
					Thread.sleep(Config.LINK_EXPLORER_SLEEP_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		stopped = true;
	}

	private void explore(Link link) { // TODO maybe merge this with
										// FormatSniffer
		if (link.isExplored()) { // TODO shouldn't be needed
			return;
		}
		if (link.getResponseCode() >= 400) {
			return;
		}
		if (!link.getHref().startsWith("http://")
				&& !link.getHref().startsWith("https://"))
			return;
		System.out.println("* Exploring " + link.getHref() + "...");
		this.link = link;
		this.url = link.getHref();
		connector.setUrl(url);

		// as "+ContentType.formatName(link.))
		String data = connector.downloadAsString(url);

		int responseCode = connector.getResponseCode();
		link.setResponseCode(responseCode);

		// System.out.println("DATA = \n"+data);
		if (data != null && responseCode == 200) {
			String contentType = connector.getContentType();
			link.setContentType(contentType);
			char format = ContentType.identifyFormat(contentType, data);
			System.out.println("Link " + link.getHref() + " recognised as "
					+ ContentType.formatName(format));
			link.setFormat(ContentType.formatName(format));

			// System.out.println("Link " + link.getHref() + " recognised as "
			// + ContentType.formatName(type));

			if (format == ContentType.UNKNOWN) { // no use
				link.setRelevance(0F);
				return;
			}
			// String contentType = connector.getContentType();
			// link.setContentType(contentType);
			// link.setFormat(ContentType.formatName(ContentType.identifyContentType(contentType)));

			RelevanceCalculator relevanceCalculator = new RelevanceCalculator();
			float relevance = relevanceCalculator.calculateRelevance(
					Config.TOPIC, data);
			System.out.println("* Link relevance = " + relevance);
			link.setRelevance(relevance);
			if (relevance > Config.SUBSCRIBE_RELEVANCE_THRESHOLD) {
				trySubscribe(link);
			}
			// System.out.println("EXPLORED " + link);
		} else {
			link.setContentType(null);
			link.setFormat(ContentType.formatName(ContentType.UNKNOWN));
			link.setRelevance(0F);
		}
		link.setExplored(true);
	}

	// private char identifyType() {
	// char type = ContentType.identifyExtension(url);
	// if (type != 0F) {
	// return type;
	// }
	// return ContentType.identifyContentType(connector.getContentType());
	// }

	private void trySubscribe(Link pageLink) { // quick and dirty feed link
												// autodiscovery
		System.out.println("* Looking for a feed linked from : "
				+ pageLink.getHref());
		HttpConnector connector = new HttpConnector();
		String content = connector.downloadAsString(pageLink.getHref());
		Set<Link> links = ContentProcessor.extractHeadLinks(
				pageLink.getOrigin(), content);
		Iterator<Link> iterator = links.iterator();
		while (iterator.hasNext()) {
			Link link = iterator.next();
			// System.out.println("LINK = "+link);
			if (link.getRel() != null && link.getRel().equals("alternate")) {
				Feed newFeed = feedList.createFeed(link.getHref());
				newFeed.init();
				newFeed.setRelevance(link.getRelevance());
				System.out.println("* Subscribing to new feed : "
						+ newFeed.getUrl());
				feedList.addFeed(newFeed);
			}
		}
	}

	public boolean isStopped() {
		return stopped;
	}

	private void updateStore(Link link) {
		System.out
				.println("* Updating link " + link.getHref() + " to store...");
		String sparql = Templater.apply("update-links",
				link.getTemplateDataMap());
		// System.out.println("\n\n----------------\n"+sparql+"\n\n---------------------");
		HttpMessage message = SparqlConnector.update(Config.UPDATE_ENDPOINT,
				sparql);
		message.setRequestBody(sparql);
		System.out.println("* link update status : " + message.getStatusCode()
				+ " " + message.getStatusMessage());
		if (message.getStatusCode() >= 400) {
			System.out.println("* " + message);
			System.out.println("* SPARQL = \n" + message.getRequestBody());
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
			System.out.println("* Explored" + value);
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

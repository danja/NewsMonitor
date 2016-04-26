/**
 * NewsMonitor
 *
 * LinkExplorer.java
 *
 * @author danja
 * @date Jun 11, 2014
 *
 * Pulls discovered links from SPARQL store, checks each for relevance where
 * appropriate applying feed autodiscovery
 *
 */
package it.danja.newsmonitor.discovery;

import it.danja.newsmonitor.io.HttpConnector;
import it.danja.newsmonitor.io.HttpMessage;
import it.danja.newsmonitor.io.SparqlConnector;
import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedList;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.LinkImpl;
import it.danja.newsmonitor.sparql.SparqlResults.Binding;
import it.danja.newsmonitor.sparql.SparqlResults.Result;
import it.danja.newsmonitor.sparql.SparqlResultsParser;
import it.danja.newsmonitor.templating.Templater;
import it.danja.newsmonitor.utils.ContentProcessor;
import it.danja.newsmonitor.utils.ContentType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class LinkExplorer implements Runnable {

	private static Logger log = LoggerFactory.getLogger(LinkExplorer.class);


	private FeedList feedList = null;
	private boolean running = false;
	private Thread thread = null;
	private boolean stopped = false;
	private Link link;
	private HttpConnector httpConnector = null;
	private SparqlConnector sparqlConnector = null;
	private String url = null;
	private TextFileReader textFileReader = null;

	private Templater templater = null;

	private Properties config;

	/**
     *
     */
	public LinkExplorer(Properties config, FeedList feedList, TextFileReader textFileReader,
			Templater templater) {
		this.config = config;
		this.feedList = feedList;
		this.templater = templater;
		this.textFileReader = textFileReader;
		sparqlConnector = new SparqlConnector(config);
		httpConnector = new HttpConnector(config);
	}

	public void start() {
		System.out.println("LINKEXPLORER START");
		running = true;
		stopped = false;
		thread = new Thread(this);
		thread.start();

	}

	public void stop() {
		running = false;
	}

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
				// log.info("LINK : " + link);
				explore(link);
				link.setExplored(true);

				updateStore(link);
				try {
					Thread.sleep(Integer.parseInt(config.getProperty("LINK_EXPLORER_SLEEP_PERIOD")));
				} catch (InterruptedException e) {
					log.error(e.getMessage());
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
				&& !link.getHref().startsWith("https://")) {
			return;
		}
		log.info("*** Exploring " + link.getHref() + "...");
		this.link = link;
		this.url = link.getHref();
		httpConnector.setUrl(url);

		// as "+ContentType.formatName(link.))
		String data = httpConnector.downloadAsString(url);

		int responseCode = httpConnector.getResponseCode();
		link.setResponseCode(responseCode);

		// log.info("DATA = \n"+data);
		if (data != null && responseCode == 200) {
			String contentType = httpConnector.getContentType();
			link.setContentType(contentType);
			char format = ContentType.identifyFormat(contentType, data);
			log.info("Link " + link.getHref() + " recognised as "
					+ ContentType.formatName(format));
			link.setFormat(ContentType.formatName(format));

			// log.info("Link " + link.getHref() + " recognised as "
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
			log.info("*** Link relevance = " + relevance);
			link.setRelevance(relevance);
			if (relevance > Float.parseFloat(config.getProperty("SUBSCRIBE_RELEVANCE_THRESHOLD"))) {
				trySubscribe(link);
			}
			// log.info("EXPLORED " + link);
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
		log.info("*** Looking for a feed linked from : " + pageLink.getHref());
		HttpConnector connector = new HttpConnector(config);
		String content = connector.downloadAsString(pageLink.getHref());
		Set<Link> links = ContentProcessor.extractHeadLinks(
				pageLink.getOrigin(), content);
		Iterator<Link> iterator = links.iterator();
		while (iterator.hasNext()) {
			Link link = iterator.next();
			// log.info("LINK = "+link);
			if (link.getRel() != null && link.getRel().equals("alternate")) {
				Feed newFeed = feedList.createFeed(link.getHref());
				newFeed.init();
				newFeed.setRelevance(link.getRelevance());
				log.info("*** Subscribing to new feed : " + newFeed.getUrl());
				feedList.addFeed(newFeed);
			}
		}
	}

	public boolean isStopped() {
		return stopped;
	}

	private void updateStore(Link link) {
		log.info("*** Updating link " + link.getHref() + " to store...");
		String sparql = templater.apply("update-links",
				link.getTemplateDataMap());
		// log.info("\n\n----------------\n"+sparql+"\n\n---------------------");
		HttpMessage message = sparqlConnector.update(config.getProperty("UPDATE_ENDPOINT"),
				sparql);
		message.setRequestBody(sparql);
		log.info("*** link update status : " + message.getStatusCode() + " "
				+ message.getStatusMessage());
		if (message.getStatusCode() >= 400) {
			log.info("*** " + message);
			log.info("*** SPARQL = \n" + message.getRequestBody());
		}
	}

	private Set<Link> getLinksFromStore() {
		
		String sparqlLocation = config.getProperty("GET_LINKS_SPARQL_LOCATION");
		System.out.println("textFileReader = "+textFileReader);
	
		String sparql = textFileReader.read(sparqlLocation);
		System.out.println("GET_LINKS_SPARQL_LOCATION");
		SparqlResultsParser parser = new SparqlResultsParser();
		parser.setSparql(sparql); // for debugging

		// throw new
		// RuntimeException("sparql = "+sparql+" getSparql() = "+parser.getSparql());
		// /*
		String xmlResults = sparqlConnector
				.query(config.getProperty("QUERY_ENDPOINT"), sparql);

		// log.info("XMLRESULTS = "+xmlResults);

		List<Result> results = parser.parse(xmlResults).getResults();
		Set<Link> links = new HashSet<Link>();
		for (int i = 0; i < results.size(); i++) {
			// log.info(results.get(i));

			Result result = results.get(i);
			// .iterator().next().getName();

			Link link = new LinkImpl();
			Iterator<Binding> iterator = result.iterator();

			while (iterator.hasNext()) {
				Binding binding = iterator.next();
				String name = binding.getName();
				String value = binding.getValue();
				setValue(link, name, value);
				// log.info("\n"+name+" = "+value);

			}
			links.add(link);
		}
		return links;
		// */
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
			log.info("*** Explored" + value);
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

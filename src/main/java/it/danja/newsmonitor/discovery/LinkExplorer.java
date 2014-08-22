/**
 * NewsMonitor
 *
 * LinkExplorer.java
 * @author danja
 * @date Jun 11, 2014
 * 
 * Pulls discovered links from SPARQL store, checks each for relevance where appropriate applying feed autodiscovery
 *
 */
package it.danja.newsmonitor.discovery;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.Set;

/**
 *
 */
public class LinkExplorer implements Runnable {

	private static Logger log = LoggerFactory.getLogger(LinkExplorer.class);
	
	private BundleContext bundleContext;
	
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
	
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
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
		log.info("*** Exploring " + link.getHref() + "...");
		this.link = link;
		this.url = link.getHref();
		connector.setUrl(url);

		// as "+ContentType.formatName(link.))
		String data = connector.downloadAsString(url);

		int responseCode = connector.getResponseCode();
		link.setResponseCode(responseCode);

		// log.info("DATA = \n"+data);
		if (data != null && responseCode == 200) {
			String contentType = connector.getContentType();
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
			if (relevance > Config.SUBSCRIBE_RELEVANCE_THRESHOLD) {
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
		log.info("*** Looking for a feed linked from : "
				+ pageLink.getHref());
		HttpConnector connector = new HttpConnector();
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
				log.info("*** Subscribing to new feed : "
						+ newFeed.getUrl());
				feedList.addFeed(newFeed);
			}
		}
	}

	public boolean isStopped() {
		return stopped;
	}

	private void updateStore(Link link) {
		log.info("*** Updating link " + link.getHref() + " to store...");
		String sparql = Templater.apply("update-links",
				link.getTemplateDataMap());
		// log.info("\n\n----------------\n"+sparql+"\n\n---------------------");
		HttpMessage message = SparqlConnector.update(Config.UPDATE_ENDPOINT,
				sparql);
		message.setRequestBody(sparql);
		log.info("*** link update status : " + message.getStatusCode()
				+ " " + message.getStatusMessage());
		if (message.getStatusCode() >= 400) {
			log.info("*** " + message);
			log.info("*** SPARQL = \n" + message.getRequestBody());
		}
	}
	
	private Set<Link> getLinksFromStore() {
		String sparql = null;
		if(Config.BUILD_TYPE == Config.STANDALONE_BUILD) {
			sparql= TextFileReader.readFromFilesystem(Config.GET_LINKS_SPARQL_FILE);
			} else {
				sparql = TextFileReader.readFromBundle(bundleContext.getBundle(), Config.GET_LINKS_SPARQL_FILE);
			}
		
		String xmlResults = SparqlConnector
				.query(Config.QUERY_ENDPOINT, sparql);

		// log.info("XMLRESULTS = "+xmlResults);

		SparqlResultsParser parser = new SparqlResultsParser();
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

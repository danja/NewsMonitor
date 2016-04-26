/**
 * TODO rename
 * NewsMonitor
 *
 * FeedImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.discovery.RelevanceCalculator;
import it.danja.newsmonitor.interpreters.FormatSniffer;
import it.danja.newsmonitor.interpreters.InterpreterFactory;
import it.danja.newsmonitor.io.HttpConnector;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Entry;
import it.danja.newsmonitor.model.EntryList;
import it.danja.newsmonitor.model.Feed;
import it.danja.newsmonitor.model.FeedEntity;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.utils.ContentType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Models a feed, wrapped around HttpConnection
 * 
 * 
 */
public class FeedImpl extends FeedEntityBase implements Feed, FeedEntity {

	private static Logger log = LoggerFactory.getLogger(FeedImpl.class);
	
	private int lives = 3;
	private boolean dead = false;

	private EntryList entryList = new EntryListImpl();

	//private Interpreter interpreter = null;

	private String subtitle = null;

	private boolean wolatile = false;

	private float relevanceFactor = 0;

	private boolean isNew = false;

	private boolean firstCall = true;

	private long lastRefresh = 0;
	private boolean pending = false;

	private Properties config;

	// private getHttpConnector() getHttpConnector();

	public FeedImpl(Properties config) {
		this.config = config;
		lives = Integer.parseInt(config.getProperty("MAX_LIVES"));
	}

	@Override
	public void setLives(int lives) {
		this.lives = lives;
	}

	@Override
	public int getLives() {
		return lives;
	}

//	@Override
//	public void setFirstCall(boolean firstCall) {
//		this.firstCall = firstCall;
//	}

	public long getLastRefresh() {
		return lastRefresh;
	}

	public String toString() {
		String string = "* Feed *\n" + getUrl() + "\nFormat = "
				+ ContentType.formatName(getFormatHint()) + "\n"
				+ getInterpreter() + "\n";

		string += entryList.toString();
		return string + super.toString();
	}

	@Override
	public void addEntry(Entry entry) {
		entryList.addEntry(entry);
	}

	@Override
	public EntryList getEntries() {
		return entryList;
	}

	@Override
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public String getSubtitle() {
		return subtitle;
	}

	@Override
	public void setVolatile(boolean v) {
		this.wolatile = v;
	}

	@Override
	public boolean isVolatile() {
		return wolatile;
	}

	@Override
	public synchronized Set<Link> getAllLinks() { // check synch
		Set<Link> links = super.getLinks();
		links.addAll(entryList.getAllLinks());
		return links;
	}

	@Override
	public synchronized Set<Link> getRemoteLinks() {
		Set<Link> all = getAllLinks();
		Set<Link> remote = new HashSet<Link>();
		Iterator<Link> iterator = all.iterator();
		while (iterator.hasNext()) {
			Link link = iterator.next();
			if (link.isRemote()) {
				remote.add(link);
			}
		}
		return remote;
	}

	@Override
	public void setRelevanceFactor(float relevanceFactor) {
		this.relevanceFactor = relevanceFactor;
	}

	@Override
	public float getRelevanceFactor() {
		return relevanceFactor;
	}

	@Override
	public void init() {
		firstCall = true;
		String url = getUrl();
		FormatSniffer sniffer = new FormatSniffer();
		char format = ContentType.UNKNOWN;

		setHttpConnector(new HttpConnector(config));
		getHttpConnector().setUrl(url);
		getHttpConnector().setConditional(false);
		getHttpConnector().setAcceptHeader(config.getProperty("FEED_ACCEPT_HEADER"));
		getHttpConnector().setUserAgentHeader(config.getProperty("FEED_USER_AGENT_HEADER"));
		log.info("\nInitializing : " + url);

		boolean streamAvailable = getHttpConnector().load();
		if (streamAvailable) {
			log.info("Sniffing...");
			// log.info("CONTENT TYPE = " +
			// connector.getContentType());
			setContentType(getHttpConnector().getContentType());
			format = sniffer.sniff(getHttpConnector());
			setFormat(ContentType.formatName(format));
			if(format == ContentType.UNAVAILABLE) {
				setLives(getLives() - 1);
				return;
			}
			// log.info("===Headers ===\n"+connector.getHeadersString()+"------\n");
		} else {
			log.info("Stream unavailable.");
			format = ContentType.UNAVAILABLE;
			setLives(getLives() - 1);
			pending  = true;
			// setDead(true);
			return;
		}

	//	log.info("FORMAT " + ContentType.formatName(format));

		if (format == ContentType.UNKNOWN || format == ContentType.RSS_SOUP) {
			if (getContentType() != null
					&& getContentType().startsWith("text/html")) {
				log.info("Appears to be HTML, taking life : " + url);
				format = ContentType.HTML;
				setLives(getLives() - 1);
				setLives(0);
				return;
			}
		}

		log.info("Format matches : " + ContentType.formatName(format));
		// setFormatName(ContentType.formatName(format));
		// log.info("Creating interpreter for feed : " + url);
		setFormatHint(format); // TODO remove duplication with
								// setInterpreter

		// interpreter = InterpreterFactory.createInterpreter(this);
		setInterpreter(InterpreterFactory.createInterpreter(this));		
		// log.info("interpreter = " + getInterpreter());
	}

	public boolean refresh() {

		// log.info("FIRSTCALL = "+firstCall);
		if (getHttpConnector() == null) { // is needed?
			setHttpConnector(new HttpConnector(config));
			getHttpConnector().setUrl(getUrl());
		}
		
		if(pending) {
			pending = false;
			log.info("IS PENDING, RE-INIT");
			init();
		}
		
		getHttpConnector().setConditional(!firstCall); // first GET is
		// unconditional
		
		isNew = getHttpConnector().load();
		setResponseCode(getHttpConnector().getResponseCode());
		// log.info("IS NEW = " + isNew);
		// log.info("STATUS =\n"+getHttpConnector().getHeadersString());

		if (isNew) {
			log.info("Connected, interpreting...");
			// log.info("interpreter = " + getInterpreter());
//			if(getInterpreter() == null) { 
//				log.info("Error, feed life lost.");
//				lives--;
//			} else {
				getInterpreter().interpret(this);
//			}
			// log.info("INTERPRETED =" + this);
			// lives = Config.MAX_LIVES;
			updateRelevance();
			isNew = true;
		} else {
			if (getHttpConnector().isDead()) {
				log.info("Error, feed life lost.");
				lives--;
				// refreshPeriod = refreshPeriod * 2;
			} else {
				log.info("Nothing new, skipping...");
			}
			isNew = false;
		}
		if (getHttpConnector().getHeaders().containsKey("Content-Type")) {
			List<String> contentTypeList = getHttpConnector().getHeaders().get(
					"Content-Type");
			setFormat(contentTypeList.get(0));
		}
		setResponseCode(getHttpConnector().getResponseCode());
		lastRefresh = now();
		// log.info("isNew = " + isNew);
		firstCall = false;
		
		return isNew;
	}

	private void updateRelevance() {
		RelevanceCalculator relevanceCalculator = new RelevanceCalculator();
		float relevance = relevanceCalculator.calculateRelevance(
				Config.TOPIC, getAllText());
		log.info("Feed relevance = "+relevance);
		setRelevance(relevance);
	}

	public boolean shouldExpire() {
		return lives < 1;
	}

	public boolean isNew() {
		return isNew;
	}

	@Override
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isDead() {
		return dead;
	}

	public void addLink(Link link) {
		super.addLink(link);
		link.setOrigin(getUrl());
	}

	@Override
	public void clean() {
		entryList = new EntryListImpl();
		super.clearLinks();
	}
	
	@Override
	public String getAllText() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getContent());
		List<Entry> entries = entryList.getEntries();
		for(int i=0;i<entries.size();i++){
			buffer.append(entries.get(i).getTitle()+"\n");
			buffer.append(entries.get(i).getContent()+"\n");
		}
		return buffer.toString();
	}

	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = super.getTemplateDataMap();
		map.put("feedUrl", getUrl()); // less confusing in templates/sparql
		map.put("htmlUrl", getHtmlUrl());
		map.put("entries", this.entryList.getTemplateList());
		map.put("entryCount", this.entryList.getEntries().size());
		map.put("subtitle", getSubtitle());
		
		map.put("format", ContentType.formatName(getFormatHint()));
		map.put("responseCode", getResponseCode());
		
		map.put("dead", isDead());
		map.put("lives", getLives());
		map.put("volatile", isVolatile());
		map.put("relevance", getRelevance());
		map.put("relevanceFactor", getRelevanceFactor());
		return map;
	}
}

/**
 * TODO rename
 * feedreader-prototype
 *
 * FeedImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.danja.feedreader.interpreters.FormatSniffer;
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.interpreters.InterpreterFactory;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Entry;
import org.danja.feedreader.model.EntryList;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.FeedEntity;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.templating.Templater;

/**
 * Models a feed, wrapped around HttpConnection
 * 
 */
public class FeedImpl extends FeedEntityBase implements Feed, FeedEntity {

	private EntryList entryList = new EntryListImpl();

	private String subtitle = null;

	private boolean wolatile = false;

	private float relevanceFactor = 0;

	public FeedImpl() {
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
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = super.getTemplateDataMap();
		map.put("feedUrl", getUrl()); // less confusing in templates/sparql
		map.put("htmlUrl", getHtmlUrl());
		map.put("entries", this.entryList.getTemplateList());
		map.put("entryCount", this.entryList.getEntries().size());
		map.put("subtitle", getSubtitle());
		map.put("dead", isDead());
		map.put("lives", getLives());
		map.put("volatile", isVolatile());
		map.put("relevanceFactor", getRelevanceFactor());
		return map;
	}

	@Override
	public void init() {
		String url = getUrl();
		FormatSniffer sniffer = new FormatSniffer();
		char format = ContentType.UNKNOWN;

		HttpConnector connector = new HttpConnector();
		connector.setUrl(url);
		connector.setConditional(false);
		System.out.println("\nInitializing : " + url);

		boolean streamAvailable = connector.load();
		if (streamAvailable) {
			System.out.println("Sniffing...");
		//	System.out.println("CONTENT TYPE = " + connector.getContentType());
			setContentType(connector.getContentType());
			format = sniffer.sniff(connector.getInputStream());
			setFormat(ContentType.formatName(format));
			// System.out.println("===Headers ===\n"+connector.getHeadersString()+"------\n");
		} else {
			System.out.println("Stream unavailable.");
			format = ContentType.UNKNOWN;
			setLives(getLives() - 1);
			// setDead(true);
			return;
		}

//		System.out.println("getFormat" + getFormat() + " "
//				+ getFormat().startsWith("text/html"));

		if (format == ContentType.UNKNOWN || format == ContentType.RSS_SOUP) {
			if (getContentType() != null
					&& getContentType().startsWith("text/html")) {
				System.out.println("Appears to be HTML, taking life : " + url);
				format = ContentType.HTML;
				setLives(getLives() - 1);
				setLives(0);
				return;
			}
		}

		System.out.println("Format matches : "
				+ ContentType.formatName(format));
		//setFormatName(ContentType.formatName(format));
		// System.out.println("Creating interpreter for feed : " + url);
		setFormatHint(format); // TODO remove duplication with
								// setInterpreter
		// feed.setRefreshPeriod(Config.getPollerPeriod());

		// interpreter = RDFInterpreterFactory.createInterpreter(format);
		// feed.setInterpreter(interpreter);

		Interpreter interpreter = InterpreterFactory.createInterpreter(this);

//		System.out.println("Setting interpreter " + interpreter + " to feed "
//				+ url);
		setInterpreter(interpreter);
		// return feed;
	}

    public void addLink(Link link) {
        super.addLink(link);
        link.setAssociatedFeedUrl(getUrl());
    }
    
	public void addAllLinks(Set<Link> links) {
		Iterator<Link> iterator = links.iterator();
		while(iterator.hasNext()) {
			Link link = iterator.next();
			link.setAssociatedFeedUrl(getUrl());
		}
		super.addAllLinks(links);
	}

	@Override
	public void clean() {
		entryList = new EntryListImpl();
		super.clearLinks();
	}
	
}

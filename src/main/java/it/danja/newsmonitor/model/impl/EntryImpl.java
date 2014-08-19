/**
 * NewsMonitor
 *
 * EntryImpl.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.model.Entry;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.utils.HtmlCleaner;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Implementation of Entry, an RSS item/Atom entry/Blog post model
 */
public class EntryImpl extends FeedEntityBase implements Entry {

    /**
	 * @return the feedUrl
	 */
	@Override
	public String getFeedUrl() {
		return feedUrl;
	}

	/**
	 * @param feedUrl the feedUrl to set
	 */
	@Override
	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	private String summary = null;
	private boolean read = false;
	private String feedUrl = null;

	/**
     * @param uriString URL of feed
     */
    public EntryImpl(String uriString) {
        super(uriString);
    }

    /**
     * Constructor
     */
    public EntryImpl() {
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	public String toString() {
		  return "* Entry *\n" + super.toString() + "content = "+getContent()+"\n";
	}

	@Override
	public void setSummary(String summary) {
		this.summary  = summary;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public boolean isRead() {
		return read;
	}
	
	@Override
    public void addLink(Link link) {
        link.setOrigin(getFeedUrl());
        super.addLink(link);
    }
    
    @Override
	public void addAllLinks(Set<Link> links) {
		Iterator<Link> iterator = links.iterator();
		while(iterator.hasNext()) {
			Link link = iterator.next();
			link.setOrigin(getFeedUrl());
		}
		super.addAllLinks(links);
		
	}
	
    @Override
	  public Map<String, Object> getTemplateDataMap(){
		  Map<String, Object> map = super.getTemplateDataMap();
		  map.put("feedUrl", getFeedUrl());
		  map.put("summary", getSummary());
		  map.put("read", isRead());
		  map.put("wordcount", getContentWordCount());
		  return map;
	  }

	@Override
	public int getContentWordCount() {
		Pattern wordPattern = Pattern.compile("(\\w+)");
		Matcher wordMatcher = wordPattern.matcher(HtmlCleaner.stripTags(getContent()));

		int count = 0;
		while (wordMatcher.find())
			count++;
		return count;
	}
}
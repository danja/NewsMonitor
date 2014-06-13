/**
 * feedreader-prototype
 *
 * EntryImpl.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.danja.feedreader.model.Entry;
import org.danja.feedreader.utils.HtmlCleaner;

/**
 *  Implementation of Entry, an RSS item/Atom entry/Blog post model
 */
public class EntryImpl extends FeedEntityBase implements Entry {

    private String summary = null;
	private boolean read = false;

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
	
	  public Map<String, Object> getTemplateDataMap(){
		  Map<String, Object> map = super.getTemplateDataMap();
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
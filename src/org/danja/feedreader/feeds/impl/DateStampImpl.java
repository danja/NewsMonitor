/**
 * feedreader-prototype
 *
 * DateStampImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import java.util.Date;

import org.danja.feedreader.feeds.DateStamp;

/**
 *
 */
public class DateStampImpl implements DateStamp {

	// is needed?
	private static String fallback; // a week ago
	static {
		Date fallbackDate = new Date(System.currentTimeMillis() - 604800000); // a week ago
		fallback = DateConverters.dateAsISO8601(fallbackDate);
	}
	
	private String published = "";
	private String updated = "";
	private String seen = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.impl.DateStamp#getPublished()
	 */
	@Override
	public String getPublished() {
		return published;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.danja.feedreader.feeds.impl.DateStamp#setPublished(java.lang.String)
	 */
	@Override
	public void setPublished(String published) {
		this.published = published;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.impl.DateStamp#getUpdated()
	 */
	@Override
	public String getUpdated() {
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.impl.DateStamp#setUpdated(java.lang.String)
	 */
	@Override
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	@Override
	public String getSeen() {
		return seen;
	}

	@Override
	public void setSeen(String seen) {
		this.seen = seen;
	}
	
	public String toString(){
		return "{seen:"+seen+", published:"+published+", updated:"+updated+"}";
	}
}
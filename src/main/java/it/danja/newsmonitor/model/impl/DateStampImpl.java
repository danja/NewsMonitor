/**
 * NewsMonitor
 *
 * DateStampImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.model.DateStamp;
import it.danja.newsmonitor.model.Templatable;
import it.danja.newsmonitor.utils.DateConverters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DateStampImpl implements DateStamp, Templatable {

	public static String FALLBACK; // a week ago
	static {
		Date fallbackDate = new Date(System.currentTimeMillis() - 604800000); // a week ago
		FALLBACK = DateConverters.dateAsISO8601(fallbackDate);
	}
	
	private String published = null;
	private String updated = null;
	private String seen = null;
	private String sortDate = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.impl.DateStamp#getPublished()
	 */
	@Override
	public String getPublished() {
		return published;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.danja.newsmonitor.model.impl.DateStamp#setPublished(java.lang.String)
	 */
	@Override
	public void setPublished(String published) {
		this.published = published;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.impl.DateStamp#getUpdated()
	 */
	@Override
	public String getUpdated() {
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.impl.DateStamp#setUpdated(java.lang.String)
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
		return "{seen:"+seen+", published:"+published+", updated:"+updated+", sortDate:"+sortDate+"}";
	}
	
	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sortDate", this.sortDate);
		map.put("seen", this.seen);
		map.put("published", this.published);
		map.put("updated", this.updated);
		return map;
	}

	@Override
	public void setSortDate(String date) {
		this.sortDate = date;
	}

	@Override
	public String getSortDate() {
		return sortDate;
	}

	@Override
	public void setToFallback() {
		this.sortDate = FALLBACK;
	}
}

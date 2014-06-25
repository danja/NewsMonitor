/**
 * feedreader-prototype
 *
 * LinkImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Templatable;

/**
 *
 */
public class LinkImpl implements Link, Templatable {

	private String associatedFeedUrl = null;
	private String rel = null;
	private String label = null;
	private String href = null;
	private boolean explored = false;
	private boolean remote = false;
	private float relevance = 0;
	private String format = null;
	private String contentType = null;
	private int responseCode = 1000;
	
	/**
	 * @return the associatedFeedUrl
	 */
	public String getAssociatedFeedUrl() {
		return associatedFeedUrl;
	}
	/**
	 * @param associatedFeedUrl the associatedFeedUrl to set
	 */
	public void setAssociatedFeedUrl(String associatedFeedUrl) {
		this.associatedFeedUrl = associatedFeedUrl;
	}
	
	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the relevance
	 */
	public float getRelevance() {
		return relevance;
	}
	/**
	 * @param relevance the relevance to set
	 */
	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setHref(java.lang.String)
	 */
	@Override
	public void setHref(String href) {
		this.href = href;
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getRel()
	 */
	@Override
	public String getRel() {
		return rel;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setRel(java.lang.String)
	 */
	@Override
	public void setRel(String rel) {
		this.rel = rel;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean isExplored() {
		return explored ;
	}
	@Override
	public void setExplored(boolean explored) {
		this.explored = explored;
	}
	
    public String toString() {
    	Map<String, Object> map = getTemplateDataMap();
    	Iterator<String> iterator = map.keySet().iterator();
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("\n--------\nLink : \n");
    	while(iterator.hasNext()) {
    		String key = iterator.next();
    		buffer.append(key + " = "+map.get(key)+"\n");
    	}
    	buffer.append("--------\n");
		return buffer.toString();
				}

    
	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", this.href);
		map.put("label", this.label);
		map.put("rel", this.rel);
		map.put("format", this.format);
		map.put("contentType", this.contentType);
		map.put("responseCode", this.responseCode);
		map.put("explored", this.explored);
		map.put("relevance", this.relevance);
		map.put("associatedFeed", this.associatedFeedUrl);
		return map;
	}
	
	@Override
	public boolean setRemote(boolean remote) {
		return this.remote  = remote;
	}
	@Override
	public boolean isRemote() {
		return remote;
	}
	@Override
	public String getContentType() {
		return contentType;
	}
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	@Override
	public void setFormat(String format) {
		this.format = format;
	}
	@Override
	public String getFormat() {
		return format;
	}
	
//	too confusing, do it longhand in the callers
//	@Override
//	public void setType(char type) {
//		setType(ContentType.formatName(type));
//	}
}

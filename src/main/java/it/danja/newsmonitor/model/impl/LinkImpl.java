/**
 * NewsMonitor
 *
 * LinkImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.Templatable;
import it.danja.newsmonitor.utils.ContentType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public class LinkImpl implements Link, Templatable {

	private String rel = null;
	private String label = null;
	private String href = null;
	private boolean explored = false;
	private boolean remote = false;
	private float relevance = 0;
	private String format = null;
	private String contentType = null;
	private int responseCode = 0;
	private String origin = null;

	// private LinkImpl(){}
	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode
	 *            the responseCode to set
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
	 * @param relevance
	 *            the relevance to set
	 */
	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Link#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Link#setHref(java.lang.String)
	 */
	@Override
	public void setHref(String href) {
		this.href = href;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Link#getRel()
	 */
	@Override
	public String getRel() {
		return rel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Link#setRel(java.lang.String)
	 */
	@Override
	public void setRel(String rel) {
		this.rel = rel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Link#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Link#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean isExplored() {
		return explored;
	}

	@Override
	public void setExplored(boolean explored) {
		this.explored = explored;
	}

	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(17, 37).append(href).toHashCode();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Link))
			return false;
		if (obj == this)
			return true;

		Link link = (Link) obj;
		return new EqualsBuilder().append(href, link.getHref()).isEquals();
	}

	public String toString() {
		Map<String, Object> map = getTemplateDataMap();
		Iterator<String> iterator = map.keySet().iterator();
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n--------\nLink : \n");
		while (iterator.hasNext()) {
			String key = iterator.next();
			buffer.append(key + " = " + map.get(key) + "\n");
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
		map.put("origin", this.origin);
		return map;
	}

	@Override
	public boolean setRemote(boolean remote) {
		return this.remote = remote;
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

	@Override
	public void setOrigin(String url) {
		this.origin = url;
	}

	@Override
	public String getOrigin() {
		return origin;
	}

	// too confusing, do it longhand in the callers
	// @Override
	// public void setType(char type) {
	// setType(ContentType.formatName(type));
	// }
}

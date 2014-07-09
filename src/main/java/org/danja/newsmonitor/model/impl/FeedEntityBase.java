/**
 * feedreader-prototype
 *
 * FeedEntityBase.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.newsmonitor.model.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.danja.newsmonitor.model.DateStamp;
import org.danja.newsmonitor.model.FeedEntity;
import org.danja.newsmonitor.model.Link;
import org.danja.newsmonitor.model.Person;
import org.danja.newsmonitor.model.Tag;
import org.danja.newsmonitor.model.Templatable;
import org.danja.newsmonitor.utils.HtmlCleaner;

/**
 * Characteristics common to components of a feed
 * 
 * can some of these be final? move more to Page/PageBase?
 */
public abstract class FeedEntityBase extends PageBase implements FeedEntity,
		Templatable {

	// private String url = null;

	private String id = null;

	private String htmlUrl = null;

	// private String content = null;

	private DateStamp datestamp = null;

	private Person author = null;

	// private Set<Link> links = new HashSet<Link>();



	private float relevance = 0;
	private boolean favourite = false;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the author
	 */
	@Override
	public Person getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(Person author) {
		this.author = author;
	}

	public FeedEntityBase() {
		super();
	}

	public FeedEntityBase(String uriString) {
		super(uriString);
	}

	// public FeedEntityBase(String uriString) {
	// this.url = uriString;
	// }





	public void setDateStamp(DateStamp date) {
		this.datestamp = date;

	}

	public DateStamp getDateStamp() {
		return datestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.newsmonitor.model.Entry#setContent(java.lang.String)
	 */
	// public void setContent(String content) {
	// // this.content = cleanContent(content);
	// this.content = content;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.newsmonitor.model.Entry#getContent()
	 */
	// public String getContent() {
	// return content;
	// }

	@Override
	public void setHtmlUrl(String url) {
		this.htmlUrl = url;
	}

	@Override
	public String getHtmlUrl() {
		return this.htmlUrl;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		// buffer.append("Entity : "+getClass().getSimpleName()+"\n");

		buffer.append("url = " + getUrl() + "\n");
		buffer.append("id = " + getId() + "\n");
		buffer.append("title = " + getTitle() + "\n");
		buffer.append("author = " + getAuthor() + "\n");
		buffer.append("links = \n");
		Iterator<Link> i = getLinks().iterator();
		while (i.hasNext()) {
			buffer.append(i.next().toString());
		}
		buffer.append("date = " + getDateStamp() + "\n\n");
		return buffer.toString();
	}

	@Override
	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}

	@Override
	public float getRelevance() {
		// TODO Auto-generated method stub
		return relevance;
	}



	@Override
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	@Override
	public boolean isFavourite() {
		return favourite;
	}



	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> data = super.getTemplateDataMap();
		// data.put("url", getUrl());
		data.put("id", getId());
		data.put("title", HtmlCleaner.escapeQuotes(getTitle()));
		data.put("content", HtmlCleaner.escapeQuotes(getContent()));
		data.put("datestamp", getDateStamp());
		data.put("author", getAuthor());
		data.put("links", getLinks());
		data.put("relevance", getRelevance());
		data.put("tags", getTags());
		data.put("favourite", isFavourite());
		return data;
	}
}
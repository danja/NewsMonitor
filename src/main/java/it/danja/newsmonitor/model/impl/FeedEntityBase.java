/**
 * NewsMonitor
 *
 * FeedEntityBase.java
 * 
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.model.DateStamp;
import it.danja.newsmonitor.model.FeedEntity;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.Person;
import it.danja.newsmonitor.model.Templatable;
import it.danja.newsmonitor.utils.HtmlCleaner;

import java.util.Iterator;
import java.util.Map;

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
	@Override
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
	@Override
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




	@Override
	public void setDateStamp(DateStamp date) {
		this.datestamp = date;

	}

	@Override
	public DateStamp getDateStamp() {
		return datestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Entry#setContent(java.lang.String)
	 */
	// public void setContent(String content) {
	// // this.content = cleanContent(content);
	// this.content = content;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Entry#getContent()
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

	@Override
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


	@Override
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
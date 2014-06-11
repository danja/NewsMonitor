/**
 * feedreader-prototype
 *
 * FeedEntityBase.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.danja.feedreader.feeds.DateStamp;
import org.danja.feedreader.feeds.FeedEntity;
import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.Person;
import org.danja.feedreader.feeds.Tag;
import org.danja.feedreader.feeds.Templatable;
import org.danja.feedreader.utils.HtmlCleaner;

/**
 * Characteristics common to components of a feed
 * 
 * can some of these be final?
 */
public abstract  class FeedEntityBase implements FeedEntity, Templatable {

    private String url = null;
    
    private String id = null;
    
	private String htmlUrl = null;
    
	private String content = null;

    private String title = null;

    private DateStamp datestamp = null;
    
    private Person author = null;
    
    private Set<Link> links = new HashSet<Link>();

	private float relevance = 0;

	private Set<Tag> tags = new HashSet<Tag>();

	private boolean favourite = false;

    /**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



    /**
	 * @return the author
	 */
	public Person getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Person author) {
		this.author = author;
	}

	public FeedEntityBase() {
    }

    public FeedEntityBase(String uriString) {
        this.url = uriString;
    }

    public void setUrl(String uriString) {
        this.url = uriString;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addLink(Link link) {
        links.add(link);
    }

	public void addAllLinks(Set<Link> links) {
		this.links.addAll(links);
	}
	
    public synchronized Set<Link> getLinks() { // TODO check synch
        return links;
    }

    public void setDateStamp(DateStamp date) {
        this.datestamp = date;

    }

    public DateStamp getDateStamp() {
        return datestamp;
    }
    
	/* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#setContent(java.lang.String)
     */
    public void setContent(String content) {
     //   this.content = cleanContent(content);
    	this.content = content;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#getContent()
     */
    public String getContent() {
        return content;
    }
    
	@Override
	public void setHtmlUrl(String url) {
		this.htmlUrl = url;
	}

	@Override
	public String getHtmlUrl() {
		return this.htmlUrl;
	}

    public Map<String, Object> getTemplateDataMap(){
    	Map<String, Object> data = new HashMap<String, Object>();
		data.put("url", getUrl());
		data.put("id", getId());
		data.put("url", getUrl());
		data.put("title", HtmlCleaner.escapeQuotes(getTitle()));
    	data.put("content", HtmlCleaner.escapeQuotes(getContent()));
    	data.put("datestamp", getDateStamp());
    	data.put("author", getAuthor());
    	data.put("links", getLinks());
    	data.put("relevance", getRelevance());
    	data.put("tags", getTags());
    	data.put("favourite", getFavourite());
		return data;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        // buffer.append("Entity : "+getClass().getSimpleName()+"\n");
      
        buffer.append("url = "+getUrl()+"\n");
        buffer.append("id = "+getId()+"\n");
        buffer.append("title = "+getTitle()+"\n");
        buffer.append("author = "+getAuthor()+"\n");
        buffer.append("links = \n");
        Iterator<Link> i = links.iterator();
        while(i.hasNext()){
        			buffer.append(i.next().toString());
        }
        buffer.append("date = "+getDateStamp()+"\n\n");
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
	public void addTag(Tag tag) {
		tags .add(tag);
	}

	@Override
	public Set<Tag> getTags() {
		return tags;
	}

	@Override
	public void setFavourite(boolean favourite) {
		this.favourite  = favourite;
	}

	@Override
	public boolean getFavourite() {
		return favourite;
	}
}
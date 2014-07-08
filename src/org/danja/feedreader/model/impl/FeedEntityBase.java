/**
 * feedreader-prototype
 *
 * FeedEntityBase.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.danja.feedreader.model.DateStamp;
import org.danja.feedreader.model.FeedEntity;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Person;
import org.danja.feedreader.model.Tag;
import org.danja.feedreader.model.Templatable;
import org.danja.feedreader.utils.HtmlCleaner;

/**
 * Characteristics common to components of a feed
 * 
 * can some of these be final?
 * move more to Page/PageBase?
 */
public abstract  class FeedEntityBase extends PageBase implements FeedEntity, Templatable {

   // private String url = null;
    
    private String id = null;
    
	private String htmlUrl = null;
    
	// private String content = null;



    private DateStamp datestamp = null;
    
    private Person author = null;
    
  //  private Set<Link> links = new HashSet<Link>();
    
    private Set<Link> links = Collections.newSetFromMap(
	        new ConcurrentHashMap<Link, Boolean>());

	private float relevance = 0;

	private Set<Tag> tags = Collections.newSetFromMap(
	        new ConcurrentHashMap<Tag, Boolean>());

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
	 * @param author the author to set
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

//    public FeedEntityBase(String uriString) {
//        this.url = uriString;
//    }

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
     * @see org.danja.feedreader.model.Entry#setContent(java.lang.String)
     */
//    public void setContent(String content) {
//     //   this.content = cleanContent(content);
//    	this.content = content;
//    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.model.Entry#getContent()
     */
//    public String getContent() {
//        return content;
//    }
    
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
	public boolean isFavourite() {
		return favourite;
	}

	public void clearLinks() {
		links = Collections.newSetFromMap(
		        new ConcurrentHashMap<Link, Boolean>());
	}
	
	   public Map<String, Object> getTemplateDataMap(){
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
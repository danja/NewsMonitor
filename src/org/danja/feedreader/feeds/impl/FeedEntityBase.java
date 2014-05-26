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

import org.danja.feedreader.content.Templater;
import org.danja.feedreader.feeds.DateStamp;
import org.danja.feedreader.feeds.FeedEntity;
import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.Person;

/**
 * Characteristics common to components of a feed
 * 
 * can some of these be final?
 */
public abstract class FeedEntityBase implements FeedEntity {

    private String url = "";
    
    private String id = "";
    
    private Set<Link> links = new HashSet<Link>();

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

	private String content = "";

    private String title = "";

    private DateStamp date = new DateStampImpl();;
    
    private Person author= new PersonImpl();

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

    public Set<Link> getLinks() {
        return links;
    }

    public void setDateStamp(DateStamp date) {
        this.date = date;

    }

    public DateStamp getDateStamp() {
        return date;
    }

    public String toHTML() {
        StringBuffer html = new StringBuffer();
        // html.append("<a href=\"" + getLink() + "\">");
        html.append(getTitle());
        html.append("</a>");
            html.append("\n<p>" + getDateStamp() + "</p>\n");
        return html.toString();
    }

    public Map<String, Object> getTemplateDataMap(){
    	Map<String, Object> data = new HashMap<String, Object>();
		data.put("url", getUrl());
		data.put("title", getTitle());
		// data.put("link", getLink());
		data.put("date", getDateStamp());
		return data;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Entity : "+getClass().getSimpleName()+"\n");
      
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
}
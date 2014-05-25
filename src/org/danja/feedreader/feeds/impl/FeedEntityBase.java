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
import java.util.Map;

import org.danja.feedreader.content.Templater;
import org.danja.feedreader.feeds.DateStamp;
import org.danja.feedreader.feeds.FeedEntity;
import org.danja.feedreader.feeds.Person;

/**
 * Characteristics common to components of a feed
 * 
 * can some of these be final?
 */
public abstract class FeedEntityBase implements FeedEntity {

    private String url = "";

    private String content = "";

    private String title = "";

    private DateStamp date = new DateStampImpl();;
    
    private String link = "";
    
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

    public void setContent(String content) {
        this.content = content;

    }

    public String getContent() {
        return content;
    }

    public void setLink(String link) {
        this.link = link;

    }

    public String getLink() {
        return link;
    }

    public void setDate(DateStamp date) {
        this.date = date;

    }

    public DateStamp getDate() {
        return date;
    }

    public String toHTML() {
        StringBuffer html = new StringBuffer();
        html.append("<a href=\"" + getLink() + "\">");
        html.append(getTitle());
        html.append("</a>");
        if (getContent().trim().length() > 0) {
            html.append("\n<p>" + getContent() + "</p>");
        }
            html.append("\n<p>" + getDate() + "</p>\n");
        return html.toString();
    }

    public Map<String, Object> getTemplateDataMap(){
    	Map<String, Object> data = new HashMap<String, Object>();
		data.put("url", getUrl());
		data.put("title", getTitle());
		data.put("description", getContent());
		data.put("link", getLink());
		data.put("date", getDate());
		return data;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Entity : "+getClass().getSimpleName()+"\n");
      
        buffer.append("url = "+getUrl()+"\n");
        buffer.append("title = "+getTitle()+"\n");
        buffer.append("content = "+getContent()+"\n");
        buffer.append("link = "+getLink()+"\n");
        buffer.append("date = "+getDate()+"\n\n");
        return buffer.toString();
    }
}
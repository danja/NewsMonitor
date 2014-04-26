/**
 * feedreader-prototype
 *
 * FeedEntityBase.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds;

import java.util.HashMap;
import java.util.Map;

import org.danja.feedreader.content.Templater;

/**
 * Characteristics common to components of a feed
 */
public abstract class FeedEntityBase implements FeedEntity {

    private String url = "";

    private String content = "";

    private String title = "";

    private String date = "";
    
    private String link = "";

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

    public void setDate(String date) {
        this.date = date;

    }

    public String getDate() {
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
        if (getDate().trim().length() > 0) {
            html.append("\n<p>" + getDate() + "</p>\n");
        }
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
        return toTurtle();
    }
}
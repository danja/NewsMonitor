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

/**
 * Characteristics common to components of a feed
 */
public class FeedEntityBase implements FeedEntity {

    private String url = "";

    private String description = "";

    private String title = "";

    private String link = "";

    private String date = "";

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

    public void setDescription(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
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
        if (getDescription().trim().length() > 0) {
            html.append("\n<p>" + getDescription() + "</p>");
        }
        if (getDate().trim().length() > 0) {
            html.append("\n<p>" + getDate() + "</p>\n");
        }
        return html.toString();
    }
}
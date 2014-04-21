/*
 * Danny Ayers Aug 27, 2004
 * http://dannyayers.com
 * 
 */
package org.danja.feedreader.feeds;

/**
 * @author danny
 *  
 */
public class EntryImpl extends FeedEntityBase implements Entry {

    private String content = "";

    public EntryImpl() {
        super();
    }

    public EntryImpl(String uriString) {
        super(uriString);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String toString() {
        return toHTML();
    }

    public String toHTML() {
        return "<div class=\"entry\">" + super.toHTML() + "<p>" + getContent()
                + "</p>" + "</div>";
    }

    // added for Poller
    private String sourceTitle;

    private String sourceLink;

    private String author;
    
    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getSourceLink() {
        return sourceLink;
    }

   
    public void setAuthor(String author) {
      this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
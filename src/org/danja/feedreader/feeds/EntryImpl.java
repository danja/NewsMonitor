/**
 * feedreader-prototype
 *
 * EntryImpl.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package org.danja.feedreader.feeds;

/**
 *  Implementation of Entry, an RSS item/Atom entry/Blog post model
 */
public class EntryImpl extends FeedEntityBase implements Entry {

	private String sourceLink = "";
	  private String sourceTitle = "";
	 
	private String author = "";
    private String content = "";

    /**
     * @param uriString URL of feed
     */
    public EntryImpl(String uriString) {
        super(uriString);
    }

    /**
     * Constructor
     */
    public EntryImpl() {
	}

	/* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#setContent(java.lang.String)
     */
    public void setContent(String content) {
        this.content = content;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#getContent()
     */
    public String getContent() {
        return content;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toHTML();
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.FeedEntityBase#toHTML()
     */
    public String toHTML() {
        return "<div class=\"entry\">" + super.toHTML() + "<p>" + getContent()
                + "</p>" + "</div>";
    }
    
    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#setSourceTitle(java.lang.String)
     */
    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#getSourceTitle()
     */
    public String getSourceTitle() {
        return sourceTitle;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#setSourceLink(java.lang.String)
     */
    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#getSourceLink()
     */
    public String getSourceLink() {
        return sourceLink;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#setAuthor(java.lang.String)
     */
    public void setAuthor(String author) {
      this.author = author;
    }

    /* (non-Javadoc)
     * @see org.danja.feedreader.feeds.Entry#getAuthor()
     */
    public String getAuthor() {
        return author;
    }
}
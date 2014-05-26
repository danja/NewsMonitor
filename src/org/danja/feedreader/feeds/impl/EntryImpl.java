/**
 * feedreader-prototype
 *
 * EntryImpl.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.danja.feedreader.content.HtmlTidy;
import org.danja.feedreader.content.Templater;
import org.danja.feedreader.feeds.Entry;

/**
 *  Implementation of Entry, an RSS item/Atom entry/Blog post model
 */
public class EntryImpl extends FeedEntityBase implements Entry {

	private HtmlTidy tidy = new HtmlTidy();
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
    	tidy.init();
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	public String toString() {
		  return "* Entry *\n" + super.toString();
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

	/**
	 * TODO remove
	 * is a big clunky, converting String to stream
	 * 
	 * @param content
	 * @return
	 */
	public String cleanContent(String content) {
		InputStream inputStream = new ByteArrayInputStream(content.getBytes());
		Writer stringWriter = new StringWriter();
		tidy.clean(inputStream, stringWriter);
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "CONTENT="+stringWriter.toString();
	}
	
	@Override
	public String toTurtle() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("body", toTurtleNoPrefixes());
		return Templater.apply("turtle-prefixes", data);
	}

	@Override
	public String toTurtleNoPrefixes() {
		Map<String, Object> data = getTemplateDataMap();
		System.out.println("--ENTRY--");
		Iterator<String> i = data.keySet().iterator();
		while(i.hasNext()){
			System.out.println(i.next()+" = "+data.get(i));
		}
		
		data.put("type", "schema:article");
		return Templater.apply("entry-turtle-no-prefixes", data);
	}
}
/**
 * feedreader-prototype
 *
 * Page.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package it.danja.newsmonitor.model;

import it.danja.newsmonitor.interpreters.Interpreter;
import it.danja.newsmonitor.io.HttpConnector;

import java.util.Set;

/**
 *
 */
public interface Page {
	public void setUrl(String url);
	public String getUrl();
	public HttpConnector getHttpConnector();
	public void setFormat(String format);
	public String getFormat();
	public void setContentType(String contentType);
	public String getContentType();
	public String getDomain();
	public void setResponseCode(int responseCode);
	public int getResponseCode();
	public void setContent(String content);
	public String getContent();
	// public void load();

	
    /**
     * Values can be ContentType.RSS2 etc.
     */
    public void setFormatHint(char hint);

    public char getFormatHint();
	public void setInterpreter(Interpreter interpreter);
	void addAllLinks(Set<Link> links);
	void addLink(Link link);
	Set<Link> getLinks();
	void clearLinks();
	void addTag(Tag tag);
	Set<Tag> getTags();
}

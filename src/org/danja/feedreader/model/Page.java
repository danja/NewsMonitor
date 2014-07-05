/**
 * feedreader-prototype
 *
 * Page.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.feedreader.model;

import org.danja.feedreader.io.HttpConnector;

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
}

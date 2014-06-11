/**
 * feedreader-prototype
 *
 * Page.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.feedreader.pages;

/**
 *
 */
public interface Page {
	public void setUrl(String url);
	public String getUrl();
	public void setContent(String content);
	public String getContent();
	public void load();

}

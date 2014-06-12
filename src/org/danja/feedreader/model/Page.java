/**
 * feedreader-prototype
 *
 * Page.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.feedreader.model;

/**
 *
 */
public interface Page {
	public void setUrl(String url);
	public String getUrl();
	public String getDomain();
	public void setContent(String content);
	public String getContent();
	public void load();
	void setLives(int lives);
	void setFirstCall(boolean firstCall);
	int getLives();

}

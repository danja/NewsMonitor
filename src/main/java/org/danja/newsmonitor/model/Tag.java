/**
 * feedreader-prototype
 *
 * Tag.java
 * @author danja
 * @date Jun 10, 2014
 *
 */
package org.danja.newsmonitor.model;

/**
 *
 */
public interface Tag {

	public void setText(String text);
	public String getText();
	public void setRelevance(float relevance);
	public float getRelevance();
}

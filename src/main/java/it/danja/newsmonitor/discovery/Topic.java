/**
 * NewsMonitor
 *
 * Topic.java
 * @author danja
 * dc:date Jun 12, 2014
 *
 */
package it.danja.newsmonitor.discovery;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Topic {
	private Map<String, Float> keywords = new HashMap<String, Float>();
	private String shortName = null;
	private String longName = null;
	
	public void setName(String shortName) {
		this.shortName = shortName;
	}
	
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public void addKeyword(String keyword, float relevance) {
		keywords.put(keyword, Float.valueOf(relevance));
	}
	
	public Map<String, Float> getKeywords() {
		return keywords;
	}
	
	public Topic merge(Topic topic){
            keywords.putAll(topic.getKeywords());
            return this;
        }
}

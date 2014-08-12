/**
 * feedreader-prototype
 *
 * FeedEntity.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.model;

import java.util.Map;
import java.util.Set;

/**
 * interface to characteristics common to several components of a feed
 * 
 * TODO extend to more elements
 */
public interface FeedEntity {

    public void setUrl(String url);
    
	public void setHtmlUrl(String url);
	
	public String getHtmlUrl();

	public void setRelevance(float relevance);
	
	public float getRelevance();
	
	public void addTag(Tag tag);
	
	public Set<Tag> getTags();
	
	public void setFavourite(boolean favourite);
	
	public boolean isFavourite();
    /**
     * @param content set the Entry content, typically HTML
     */
    public void setContent(String content);

    /**
     * @return get the Entry content, typically HTML
     */
    public String getContent();
    
    public String getUrl();
    
    public void setId(String id);
    
    public String getId();

    public void setTitle(String title);

    public String getTitle();

    // public String getLink();
    public void addLink(Link link);  

    public Set<Link> getLinks();

    public void setDateStamp(DateStamp date);

    public DateStamp getDateStamp();
    
    public void setAuthor(Person author);
    
    public Person getAuthor();
    
	public Map<String, Object> getTemplateDataMap();
    
    /**
     * Gets an RDF/Turtle representation of the FeedEntity (sans prefixes)
     * 
     * @return the Turtle
     */
   // public String toTurtle();
}
/**
 * feedreader-prototype
 *
 * FeedEntity.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds;

import java.util.Set;

/**
 * interface to characteristics common to several components of a feed
 * 
 * TODO extend to more elements
 */
public interface FeedEntity {

    public void setUrl(String url);

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
    
    /**
     * Gets an RDF/Turtle representation of the FeedEntity (sans prefixes)
     * 
     * @return the Turtle
     */
    public String toTurtle();
}
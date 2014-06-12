/**
 * TODO rename
 * feedreader-prototype
 *
 * xxx.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.model;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.danja.feedreader.interpreters.Interpreter;

/**
 * syndication-oriented access to HTTP operations
 *  
 */
public interface Feed extends FeedEntity, Page {
	
	/**
	 * @param subtitle
	 */
	public void setSubtitle(String subtitle);
	
	/**
	 * @return
	 */
	public String getSubtitle();
	
	public void addEntry(Entry entry);
    /**
     * Values can be FeedConstants.RSS2 etc.
     */
    public void setFormatHint(char hint);

    public char getFormatHint();

    public boolean refresh();

    public Set<Link> getAllLinks();
   // public void setRefreshPeriod(long refreshPeriod);

    /**
     * @return is this feed up-to-date?
     */
    public boolean isNew();

    /**
     * @return is the site responding?
     */
    public boolean isDead();

    public boolean shouldExpire();

    public String getETag();

    public String getLastModified();

    public String getContentEncoding();

    public InputStream getInputStream();

    public String getStatus();

    public void downloadToFile(String filename);
    
    public void setInterpreter(Interpreter interpreter);
    
    public void setDead(boolean dead);
    
    public void setLives(int lives);
    
    public int getLives();
    
    public void setVolatile(boolean v);
    
    public boolean isVolatile();
    
	public void setRelevanceFactor(float relevanceFactor);
	
	public float getRelevanceFactor();
    
    
    /**
     * Gets an RDF/Turtle representation of the EntryList (sans prefixes)
     * 
     * @return the Turtle
     */
  //  public String toTurtle();
	public EntryList getEntries();
	public void setFirstCall(boolean firstCall);

	public Map<String, Object> getTemplateDataMap();

	public Set<Link> getRemoteLinks();
}
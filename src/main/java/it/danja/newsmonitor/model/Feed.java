/**
 * NewsMonitor
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.model;

import it.danja.newsmonitor.interpreters.Interpreter;

import java.io.InputStream;
import java.util.Set;

/**
 * syndication-oriented access to HTTP operations
 *  
 */
public interface Feed extends FeedEntity, Page, Templatable {
	
	public void setLives(int lives);
	// public void setFirstCall(boolean firstCall);
	public int getLives();
	public void setDead(boolean dead);
	public boolean isDead();
	/**
	 * @param subtitle
	 */
	public void setSubtitle(String subtitle);
	
	/**
	 * @return
	 */
	public String getSubtitle();
	
	public void addEntry(Entry entry);


    public boolean refresh();

    public Set<Link> getAllLinks();
   // public void setRefreshPeriod(long refreshPeriod);

    /**
     * @return is this feed up-to-date?
     */
    public boolean isNew();


    public boolean shouldExpire();

    public String getETag();

    public String getLastModified();

    public String getContentEncoding();

    public InputStream getInputStream();

    public String getStatus();

    // public void downloadToFile(String filename);
    
    public void setInterpreter(Interpreter interpreter);
    
    public void setVolatile(boolean v);
    
    public boolean isVolatile();
    
	public void setRelevanceFactor(float relevanceFactor);
	
	public float getRelevanceFactor();
    
	public String getAllText();
	
	public EntryList getEntries();

	public Set<Link> getRemoteLinks();

	public void init();

	public void clean();
	public void addAllLinks(Set<Link> contentLinks);
}
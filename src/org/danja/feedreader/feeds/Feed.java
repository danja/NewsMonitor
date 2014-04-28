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
package org.danja.feedreader.feeds;

import java.io.InputStream;

import org.danja.feedreader.interpreters.Interpreter;

/**
 * syndication-oriented access to HTTP operations
 *  
 */
public interface Feed extends FeedEntity {

	public void addEntry(Entry entry);
    /**
     * Values can be FeedConstants.RSS2 etc.
     */
    public void setFormatHint(char hint);

    public char getFormatHint();

    public boolean refresh();

    public void setRefreshPeriod(long refreshPeriod);

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
    
    /**
     * Gets an RDF/Turtle representation of the EntryList (sans prefixes)
     * 
     * @return the Turtle
     */
    public String toTurtle();
	public EntryList getEntries();
	public void setFirstCall(boolean firstCall);
}
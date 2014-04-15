package org.danja.feedreader.feeds;

import java.io.InputStream;

import org.danja.feedreader.io.Interpreter;

/**
 * syndication-oriented access to HTTP operations
 * 
 * @version $Revision$
 *  
 */
public interface FeedFetcher extends FeedEntity {

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
}
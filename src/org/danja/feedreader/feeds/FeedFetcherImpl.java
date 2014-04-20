package org.danja.feedreader.feeds;

import java.io.InputStream;
import java.util.Date;

import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.Interpreter;

/**
 * Models a feed, wrapped around HttpConnection
 * 
 * @version $Revision$
 *  
 */
public class FeedFetcherImpl extends FeedEntityBase implements FeedFetcher,
        FeedEntity {

    private long lastRefresh;

    private long refreshPeriod;

    private static final double ditherFactor = 0.1D;

    private int MAX_LIVES = 3;
    
    private int lives = MAX_LIVES;

    private char hint = FeedConstants.UNKNOWN;

    private boolean isNew = false;

    private HttpConnector httpConnector = null;

    private Interpreter interpreter;

    public FeedFetcherImpl(String uri) {
        super(uri);
    }



    public boolean refresh() {
        if (now() < lastRefresh + refreshPeriod + getPeriodDither()) {
            return false;
        }
        if (httpConnector == null) {
            httpConnector = new HttpConnector(getURIString());
        }
        isNew = httpConnector.load();

        if (isNew) {
            System.out.println("Connected, interpreting...");
            interpreter.interpret(this);
            lives = MAX_LIVES;
        } else {
            if (httpConnector.isDead()) {
                System.out.println("Error, feed life lost.");
                lives--;
                refreshPeriod = refreshPeriod*2;
            }
        }
        lastRefresh = now();
        return isNew;
    }

    private long now() {
        return (new Date()).getTime();
    }

    private long getPeriodDither() {
        return (long) (Math.random() * ditherFactor * refreshPeriod);
    }
    
    public boolean shouldExpire() {
        return lives < 1;
    }

    public void setFormatHint(char hint) {
        this.hint = hint;
    }

    public char getFormatHint() {
        return hint;
    }
    
    public Interpreter getInterpreter() {
        return interpreter;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isDead() {
        return httpConnector.isDead();
    }

    public String getETag() {
        return httpConnector.getETag();
    }

    public String getLastModified() {
        return httpConnector.getLastModified();
    }

    public String getContentEncoding() {
        return httpConnector.getContentEncoding();
    }

    public String getContentType() {
        return httpConnector.getContentType();
    }

    public InputStream getInputStream() {
        return httpConnector.getInputStream();
    }

    public String getStatus() {
        return httpConnector.getStatus();
    }

    public void downloadToFile(String filename) {
        httpConnector.downloadToFile(filename);
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public long getRefreshPeriod() {
        return refreshPeriod;
    }


    public void setRefreshPeriod(long refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }
}
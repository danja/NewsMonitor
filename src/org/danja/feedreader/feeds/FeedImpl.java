/**
 * TODO rename
 * feedreader-prototype
 *
 * FeedImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.feeds;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.danja.feedreader.content.Templater;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.main.Config;

/**
 * Models a feed, wrapped around HttpConnection
 *  
 */
public class FeedImpl extends FeedEntityBase implements Feed,
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

    public FeedImpl() {
    }



    public boolean refresh() {
        if (now() < lastRefresh + refreshPeriod + getPeriodDither()) {
            return false;
        }
        if (httpConnector == null) {
        	httpConnector = new HttpConnector();
        	String url = getUrl();
        	System.out.println("URL in FeedImpl.refresh = "+url);
        	httpConnector.setUrl(url);
        }
        isNew = httpConnector.load();

        if (isNew) {
            System.out.println("Connected, interpreting...");
            System.out.println("interpretor ="+interpreter);
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
    

    
	@Override
	public String toTurtle() {
		Map<String, Object> data = getTemplateDataMap();
		data.put("type", "rss:channel");
		Iterator<String> i = data.keySet().iterator();
		while(i.hasNext()){
			System.out.println(i.next()+" = "+data.get(i));
		}

		return Templater.apply("feed-turtle", data);
	}
}
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
package org.danja.feedreader.feeds.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.danja.feedreader.feeds.Entry;
import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedEntity;
import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.templating.Templater;

/**
 * Models a feed, wrapped around HttpConnection
 * 
 */
public class FeedImpl extends FeedEntityBase implements Feed, FeedEntity {

	private EntryList entryList = new EntryListImpl();

	private long lastRefresh;

//	private long refreshPeriod;

	private static final double ditherFactor = 0.1D;

	private int lives = Config.MAX_LIVES;

	private char hint = FeedConstants.UNKNOWN;

	private boolean isNew = false;

	private HttpConnector httpConnector = null;

	private Interpreter interpreter;

	private boolean firstCall;

	private String htmlUrl;

	public FeedImpl() {
	}

	public boolean refresh() {
//		 if (now() < lastRefresh + refreshPeriod + getPeriodDither()) {
//		 return false;
//		 }

		if (httpConnector == null) {
			httpConnector = new HttpConnector();

			httpConnector.setConditional(!firstCall); // first GET is
														// unconditional

			String url = getUrl();
			System.out.println("FeedImpl.refresh = " + url);
			httpConnector.setUrl(url);
		}
		isNew = httpConnector.load();

		if (isNew) {
			System.out.println("Connected, interpreting...");
			System.out.println("interpreter =" + interpreter);
			interpreter.interpret(this);
			// System.out.println("INTERPRETED =" + this);
			lives = Config.MAX_LIVES;
			isNew = false;
		} else {
			if (httpConnector.isDead()) {
				System.out.println("Error, feed life lost.");
				lives--;
			//	refreshPeriod = refreshPeriod * 2;
			}
			System.out.println("Nothing new, skipping...");
		}
		lastRefresh = now();
		// System.out.println("isNew = " + isNew);
		return isNew;
	}

	private long now() {
		return (new Date()).getTime();
	}

//	private long getPeriodDither() {
//		return (long) (Math.random() * ditherFactor * refreshPeriod);
//	}

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

//	public long getRefreshPeriod() {
//		return refreshPeriod;
//	}

//	public void setRefreshPeriod(long refreshPeriod) {
//		this.refreshPeriod = refreshPeriod;
//	}

	public long getLastRefresh() {
		return lastRefresh;
	}

//	@Override
//	public String toTurtle() {
//		Map<String, Object> data = getTemplateDataMap();
//		data.put("type", "rss:channel");
//
//		System.out.println(Templater.dataMapToString(data));
//		System.out.println("--FEED--");
//		return Templater.apply("feed-turtle", data);
//	}

	public String toString() {
		String string = "* Feed *\n" + getUrl() + "\nFormat = "
				+ FeedConstants.formatName(getFormatHint()) + "\n"
				+ interpreter +"\n";
		
		string += entryList.toString();
		return string+super.toString();
	}

	@Override
	public void addEntry(Entry entry) {
		entryList.addEntry(entry);
	}

	@Override
	public EntryList getEntries() {
		return entryList;
	}

	@Override
	public void setFirstCall(boolean firstCall) {
		this.firstCall = firstCall;
	}

	@Override
	public void setHtmlUrl(String url) {
		this.htmlUrl = url;
	}

	@Override
	public String getHtmlUrl() {
		return this.htmlUrl;
	}
	
	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = super.getTemplateDataMap();
		map.put("htmlUrl", this.htmlUrl);
		map.put("dead", isDead());
		map.put("entries", this.entryList.getTemplateList());
		map.put("entryCount", this.entryList.getEntries().size());
		return map;
	}
}

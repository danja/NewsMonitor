package org.danja.feedreader.model.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Page;

public abstract class PageBase implements Page {
	
	private HttpConnector httpConnector = null;

	private Interpreter interpreter = null;
	
	private boolean isNew = false;

	private boolean firstCall = true;
	
	private int lives = Config.MAX_LIVES;
	
	private long lastRefresh = 0;

	private String url = null;
	
    private String title = null;

	private String content = null;

	public PageBase(String url) {
this.url = url;
	}

    public PageBase() {
	}

	public void setUrl(String uriString) {
        this.url = uriString;
    }

    public String getUrl() {
        return url ;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    public String getDomain(){
    	String[] split = getUrl().split("/");
    	return split[3];
    }

	@Override
	public void setContent(String content) {
		this.content  = content;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	public boolean refresh() {
//		 if (now() < lastRefresh + refreshPeriod + getPeriodDither()) {
//		 return false;
//		 }

		if (httpConnector == null) {
			httpConnector = new HttpConnector();
			httpConnector.setConditional(!firstCall); // first GET is unconditional

			String url = getUrl(); // ?? refactor?
			httpConnector.setUrl(url);
		}
		isNew = httpConnector.load();
	//	System.out.println("STATUS =\n"+httpConnector.getHeadersString());

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
	
	public boolean shouldExpire() {
		return lives < 1;
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

	public long getLastRefresh() {
		return lastRefresh;
	}
	
	@Override
	public void setFirstCall(boolean firstCall) {
		this.firstCall = firstCall;
	}
	
	@Override
	public void setLives(int lives) {
		this.lives = lives;
	}

	@Override
	public int getLives() {
		return lives;
	}

	protected long now() {
		return (new Date()).getTime();
	}

	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", getUrl());
		return map;
	}
}

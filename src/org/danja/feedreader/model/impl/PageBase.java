package org.danja.feedreader.model.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.danja.feedreader.interpreters.Interpreter;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.ContentType;
import org.danja.feedreader.model.Page;

public abstract class PageBase implements Page {

	private HttpConnector httpConnector = null;

	private Interpreter interpreter = null;
	
	private String contentType = null;

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private boolean isNew = false;

	private boolean firstCall = true;

	private int lives = Config.MAX_LIVES;

	private long lastRefresh = 0;

	private String url = null;
	
	private char formatHint = ContentType.UNKNOWN;

	private String title = null;

	private String content = null;

	private String format = null;
	
	private boolean dead = false;

	private int responseCode = 0;
	
	

	public PageBase(String url) {
		this.url = url;
	}

	public PageBase() {
	}

	public void setUrl(String uriString) {
		this.url = uriString;
	}

	public String getUrl() {
		return url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getDomain() {
		String[] split = getUrl().split("/");
		return split[3];
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}

//	@Override
//	public void load() { // used by LinkExplorer
//		HttpConnector connector = new HttpConnector();
//		connector.setUrl(url);
//		connector.setConditional(false);
//		System.out.println("\n\nGetting content for : " + url);
//
//		boolean streamAvailable = connector.load();
//		if (streamAvailable) {
//			FormatSniffer sniffer = new FormatSniffer();
//			 setFormatHint(sniffer.sniff(connector.getInputStream()));
//			 
////			System.out.println("streamAvailable ===Headers ===\n"
////					+ connector.getHeadersString() + "------\n");
//		} else {
//			System.out.println("Stream unavailable.");
//			// format = format.UNKNOWN;
//		}
//		// System.out.println("Format matches : "
//		// + format.formatName(format));
//	//	System.out.println("\nCreating object for Page : " + url);
//		// connector.
//	}

	public boolean refresh() {

		if (httpConnector == null) {
			httpConnector = new HttpConnector();
			httpConnector.setConditional(!firstCall); // first GET is unconditional

			String url = getUrl(); // ?? refactor?
			httpConnector.setUrl(url);
		}
		isNew = httpConnector.load();
	//	System.out.println("IS NEW = " + isNew);
		// System.out.println("STATUS =\n"+httpConnector.getHeadersString());

		if (isNew) {
			System.out.println("Connected, interpreting...");
			System.out.println("interpreter =" + interpreter);
			interpreter.interpret(this);
			// System.out.println("INTERPRETED =" + this);
		//	lives = Config.MAX_LIVES;
			isNew = true;
		} else {
			if (httpConnector.isDead()) {
				System.out.println("Error, feed life lost.");
				lives--;
				// refreshPeriod = refreshPeriod * 2;
			} else {
			System.out.println("Nothing new, skipping...");
			}
			isNew = false;
		}
		if (httpConnector.getHeaders().containsKey("Content-Type")) {
			List<String> contentTypeList = httpConnector.getHeaders().get(
					"Content-Type");
			format = contentTypeList.get(0);
		}
		responseCode = httpConnector.getResponseCode();
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
	
	@Override
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isDead() {
		return dead;
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
	
	
	@Override
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getFormat() {
		return format;
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
	
	@Override
	public void setResponseCode(int responseCode) {
		this.responseCode  = responseCode;
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}
	
	@Override
	public void setFormatHint(char hint) {
		this.formatHint = hint;
	}

	@Override
	public char getFormatHint() {
		return formatHint;
	}

	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", getUrl());
		map.put("responseCode", getResponseCode());
		map.put("contentType", getContentType());
		map.put("format", getFormat());
		// private String formatName = format.formatName(format.UNKNOWN);
		return map;
	}
}

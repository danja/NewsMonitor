package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.interpreters.Interpreter;
import it.danja.newsmonitor.io.HttpConnector;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.LinkSet;
import it.danja.newsmonitor.model.Page;
import it.danja.newsmonitor.model.Tag;
import it.danja.newsmonitor.utils.ContentType;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PageBase implements Page {

	private HttpConnector httpConnector = null;

	private Interpreter interpreter = null;

	private String contentType = null;

	// private Set<Link> links = Collections
	// .newSetFromMap(new ConcurrentHashMap<Link, Boolean>());
	//
	// private Set<String> hrefs = Collections
	// .newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	private LinkSet links = new LinkSetImpl();

	private Set<Tag> tags = Collections
			.newSetFromMap(new ConcurrentHashMap<Tag, Boolean>());

	@Override
	public void addLink(Link link) {
		links.add(link);
	}

	@Override
	public void addAllLinks(Set<Link> links) { // TODO can refactor?
		links.addAll(links);
	}

	@Override
	public synchronized Set<Link> getLinks() { // TODO check sync
		return links.getLinks();
	}

	@Override
	public void clearLinks() {
		links.clear();
	}

	@Override
	public void addTag(Tag tag) {
		tags.add(tag);
	}

	@Override
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private String url = null;

	private char formatHint = ContentType.UNKNOWN;

	private String title = null;

	private String content = null;

	private String format = null;

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

	// @Override
	// public void load() { // used by LinkExplorer
	// HttpConnector connector = new HttpConnector();
	// connector.setUrl(url);
	// connector.setConditional(false);
	// log.info("\n\nGetting content for : " + url);
	//
	// boolean streamAvailable = connector.load();
	// if (streamAvailable) {
	// FormatSniffer sniffer = new FormatSniffer();
	// setFormatHint(sniffer.sniff(connector.getInputStream()));
	//
	// // log.info("streamAvailable ===Headers ===\n"
	// // + connector.getHeadersString() + "------\n");
	// } else {
	// log.info("Stream unavailable.");
	// // format = format.UNKNOWN;
	// }
	// // log.info("Format matches : "
	// // + format.formatName(format));
	// // log.info("\nCreating object for Page : " + url);
	// // connector.
	// }

	public Interpreter getInterpreter() {
		return interpreter;
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

	@Override
	public void setInterpreter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}

	protected long now() {
		return (new Date()).getTime();
	}

	@Override
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
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

	/**
	 * @return the httpConnector
	 */
	public HttpConnector getHttpConnector() {
		return httpConnector;
	}

	/**
	 * @param httpConnector
	 *            the httpConnector to set
	 */
	public void setHttpConnector(HttpConnector httpConnector) {
		this.httpConnector = httpConnector;
	}
}

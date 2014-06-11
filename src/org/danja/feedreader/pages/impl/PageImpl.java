package org.danja.feedreader.pages.impl;

import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.pages.Page;

public class PageImpl implements Page {

	private String url = null;
	private String content = null;

	@Override
	public void setUrl(String url) {
		this.url  = url;
	}

	@Override
	public String getUrl() {
		return url;
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
		HttpConnector connector = new HttpConnector();
		connector.setUrl(url);
		connector.setConditional(false);
		System.out.println("\n\nGetting content for : " + url);

		boolean streamAvailable = connector.load();
		if (streamAvailable) {
			// format = sniffer.sniff(connector.getInputStream());
			// System.out.println("===Headers ===\n"+connector.getHeadersString()+"------\n");
		} else {
			System.out.println("Stream unavailable.");
			// format = FeedConstants.UNKNOWN;
		}
//		System.out.println("Format matches : "
//				+ FeedConstants.formatName(format));
		System.out.println("\nCreating object for Page : " + url);
		// connector.
	}

}

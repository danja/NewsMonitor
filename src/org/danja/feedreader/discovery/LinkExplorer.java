/**
 * feedreader-prototype
 *
 * LinkExplorer.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.feedreader.discovery;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.danja.feedreader.interpreters.HtmlHandler;
import org.danja.feedreader.interpreters.SoupParser;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.model.Feed;
import org.danja.feedreader.model.FeedConstants;
import org.danja.feedreader.model.FeedList;
import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Page;
import org.danja.feedreader.model.impl.PageImpl;

/**
 *
 */
public class LinkExplorer implements Runnable {

	private FeedList feedList = null;
	private boolean running = false;
	private Thread thread = null;

	/**
	 * 
	 */
	public LinkExplorer(FeedList feedList) {
		this.feedList = feedList;
	}

	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();

	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		while (running) {
			Set<Link> links = feedList.getAllLinks();

			Iterator<Link> linkIterator = links.iterator();
			while (linkIterator.hasNext()) {
				explore(linkIterator.next());
			}
			try {
				Thread.sleep(Config.LINK_EXPLORER_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void explore(Link link) {
		String url = link.getHref();
		loadPage(url);
		
		link.setExplored(true);
	}

	// private void parseHTML(String uriString) {
	//
	// try {
	// SoupParser parser = new SoupParser();
	// HtmlHandler handler = new HtmlHandler();
	//
	// parser.setHandler(handler);
	//
	// HttpConnector connector = new HttpConnector();
	// connector.setUrl(uriString);
	// boolean streamAvailable = connector.load();
	// if (streamAvailable) {
	// InputStream inputStream = connector.getInputStream();
	// parser.parse(inputStream);
	// // links = handler.getLinks();
	// // comments = handler.getComments();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private Page loadPage(String url) {
		Page page = new PageImpl(url);
		page.load();
		return page;

	}

}

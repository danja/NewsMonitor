/**
 * feedreader-prototype
 *
 * LinkExplorer.java
 * @author danja
 * @date Jun 11, 2014
 *
 */
package org.danja.feedreader.discovery;

import java.util.List;

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.main.Config;

/**
 *
 */
public class LinkExplorer  implements Runnable {

	private FeedList feedList = null;
	private boolean running = false;
	private Thread thread = null;

	/**
	 * 
	 */
	public LinkExplorer(FeedList feedList) {
		this.feedList  = feedList;
	}

	public void start() {
		running = true;
		thread  = new Thread(this);
		thread.start();
		
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		while(running) {
			List<Feed> feeds = feedList.getList();
		//	for(int i=0;i<feeds.size();)
			try {
				Thread.sleep(Config.LINK_EXPLORER_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

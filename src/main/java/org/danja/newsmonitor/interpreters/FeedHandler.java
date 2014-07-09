package org.danja.newsmonitor.interpreters;

import org.danja.newsmonitor.model.Feed;
import org.xml.sax.ContentHandler;

public interface FeedHandler extends ContentHandler {

	public void setFeed(Feed feed);

	public Feed getFeed();

}
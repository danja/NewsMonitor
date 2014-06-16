package org.danja.feedreader.interpreters;

import org.danja.feedreader.model.Feed;
import org.xml.sax.ContentHandler;

public interface FeedHandler extends ContentHandler {

	public void setFeed(Feed feed);

	public Feed getFeed();

}
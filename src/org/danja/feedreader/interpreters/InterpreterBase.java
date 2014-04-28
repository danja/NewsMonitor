package org.danja.feedreader.interpreters;

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.parsers.FeedHandler;

public abstract class InterpreterBase implements Interpreter {

	private FeedHandler contentHandler;

	@Override
	public void setContentHandler(FeedHandler feedHandler) {
		this.contentHandler = feedHandler;
	}


	@Override
	public FeedHandler getContentHandler() {
		return contentHandler;
	}

}

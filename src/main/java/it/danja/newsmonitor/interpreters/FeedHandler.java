package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Feed;
import org.xml.sax.ContentHandler;

public interface FeedHandler extends ContentHandler {
  public void setFeed(Feed feed);

  public Feed getFeed();
}

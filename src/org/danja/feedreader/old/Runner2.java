// TODO RETIRE ME

package org.danja.feedreader.old;

import java.util.Iterator;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.feeds.FeedFetcherImpl;
import org.danja.feedreader.feeds.FeedSet;
import org.danja.feedreader.feeds.FeedSetImpl;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.parsers.InterpreterFactory;

public class Runner2 {

    static int REFRESH_PERIOD = 10000; // milliseconds

    static EntryList entries = new EntryListImpl(); //++

    public static void main(String[] args) {

        FeedSet feeds = new FeedSetImpl();

        FeedFetcher feed1 = new FeedFetcherImpl(
                "http://martinfowler.com/bliki/bliki.rss");
        feed1.setTitle("feed_1");
        Interpreter interpreter1 = InterpreterFactory.createInterpreter(
                FeedConstants.RSS2, entries);
        feed1.setInterpreter(interpreter1); // ++
        feed1.setRefreshPeriod(REFRESH_PERIOD);
        feeds.addFeed(feed1);

        FeedFetcher feed2 = new FeedFetcherImpl(
                "http://dannyayers.com/feed/rss2");
        feed2.setTitle("feed_2");
        Interpreter interpreter2 = InterpreterFactory.createInterpreter(
                FeedConstants.RSS2_BOZO, entries);
        feed2.setInterpreter(interpreter2); // ++
        feed2.setRefreshPeriod(REFRESH_PERIOD);
        feeds.addFeed(feed2);

        while (true) {
            feeds.refreshAll();
            displayStatus(feeds);
        }
    }

    private static void displayStatus(FeedSet feeds) {
        Iterator feedIterator = feeds.getFeedCollection().iterator();
        while (feedIterator.hasNext()) {
            System.out.println(((FeedFetcher) feedIterator.next()).getStatus());
        }
        System.out.println("---------------");
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(entries.getEntry(i));
        }
    }
}
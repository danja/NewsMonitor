/** TODO RETIREME
*/
package org.danja.feedreader.feeds;

import java.util.Iterator;
import java.util.Set;

import org.danja.feedreader.io.ChannelSetReader;
import org.danja.feedreader.io.DownloadInterpreter;

public class Runner {

    static int REFRESH_PERIOD = 10000; // milliseconds

    public static void main(String[] args) {

        ChannelSetReader channelSetReader = new ChannelSetReader();
        Set feedlistURIs = channelSetReader.load(args[0]);
        FeedSet feeds = new FeedSetImpl();

        Iterator feedIterator = feedlistURIs.iterator();
        FeedFetcher feed;
        int nameCounter = 1;
        while (feedIterator.hasNext()) {
            feed = new FeedFetcherImpl((String) feedIterator.next());
            feed.setTitle("feed_" + Integer.toString(nameCounter++));
            feed.setInterpreter(new DownloadInterpreter(args[1]));
            feed.setRefreshPeriod(REFRESH_PERIOD);
            feeds.addFeed(feed);
        }

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
    }
}
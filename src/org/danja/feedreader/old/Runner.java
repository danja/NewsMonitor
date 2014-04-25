/** TODO RETIREME
*/
package org.danja.feedreader.old;

import java.util.Iterator;
import java.util.Set;

import org.danja.feedreader.feeds.Feed;
import org.danja.feedreader.feeds.FeedImpl;
import org.danja.feedreader.feeds.FeedList;
import org.danja.feedreader.feeds.FeedListImpl;
import org.danja.feedreader.io.DownloadInterpreter;

public class Runner {

    static int REFRESH_PERIOD = 10000; // milliseconds

    public static void main(String[] args) {

        ChannelSetReader channelSetReader = new ChannelSetReader();
        Set feedlistURIs = channelSetReader.load(args[0]);
        FeedList feeds = new FeedListImpl();

        Iterator feedIterator = feedlistURIs.iterator();
        Feed feed;
        int nameCounter = 1;
        while (feedIterator.hasNext()) {
            feed = new FeedImpl((String) feedIterator.next());
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

    private static void displayStatus(FeedList feeds) {
        Iterator feedIterator = feeds.getFeedCollection().iterator();
        while (feedIterator.hasNext()) {
            System.out.println(((Feed) feedIterator.next()).getStatus());
        }
        System.out.println("---------------");
    }
}
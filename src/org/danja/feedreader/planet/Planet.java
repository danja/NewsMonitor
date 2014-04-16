package org.danja.feedreader.planet;

import java.util.Iterator;
import java.util.Set;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.feeds.FeedFetcherImpl;
import org.danja.feedreader.feeds.FeedSet;
import org.danja.feedreader.feeds.FeedSetImpl;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.io.OpmlSetReader;
import org.danja.feedreader.parsers.InterpreterFactory;

public class Planet {

    static int REFRESH_PERIOD = 10000; // milliseconds

    static int MAX_ITEMS = 5;

    static EntryList entries = new EntryListImpl(); //++

    public static void main(String[] args) {
        Planet planet = new Planet();
        Set channelURIs = planet.loadChannelList("input/bloggers.rdf");
        // Set channelURIs = planet.loadChannelList("input/feedlist.opml");
        FeedSet feeds = planet.initFeeds(channelURIs);
        FileEntrySerializer serializer = new FileEntrySerializer();
        serializer.loadDocumentShell("input/shell.xml");

        while (true) {
            feeds.refreshAll();
            //    displayStatus(feeds);
        
            entries.trimList(MAX_ITEMS);
            
           serializer.clearEntries();
       
            for (int i = 0; i < entries.size(); i++) {
                serializer.addEntry(entries.getEntry(i));
            }
            System.out.println("Writing RSS 2.0...");
            serializer.write("output/rss.xml");
            System.out.println("Writing HTML...");
            serializer.transformWrite("output/index.html",
                    "templates/rss2html.xslt");
            System.out.println("Writing RSS 1.0...");
            serializer.transformWrite("output/feed.rdf",
                    "templates/feed-rss1.0.xsl");
        }
    }

    public Set loadChannelList(String filename) {
        OpmlSetReader reader = new OpmlSetReader();
        return reader.load(filename);
    }

    public FeedSet initFeeds(Set channelURIs) {
        FeedSet feeds = new FeedSetImpl();
        Iterator channelIterator = channelURIs.iterator();
        FeedFetcher feedFetcher;
        Interpreter interpreter;
        String uriString;
        while (channelIterator.hasNext()) {
            uriString = (String) channelIterator.next();
            feedFetcher = new FeedFetcherImpl(uriString);
            interpreter = InterpreterFactory.createInterpreter(
                    FeedConstants.RSS2_BOZO, entries);
            feedFetcher.setInterpreter(interpreter); // ++
            feedFetcher.setRefreshPeriod(REFRESH_PERIOD);
            feeds.addFeed(feedFetcher);
        }
        return feeds;
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
package org.danja.feedreader.social;

import java.util.Iterator;
import java.util.Set;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.feeds.FeedFetcherImpl;
import org.danja.feedreader.feeds.FeedSet;
import org.danja.feedreader.feeds.FeedSetImpl;
import org.danja.feedreader.io.ChannelSetReader;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.planet.FileEntrySerializer;

public class Supervisor {

    static int REFRESH_PERIOD = 10000; // milliseconds

    static int MAX_ITEMS = 5;

    static EntryList entries = new EntryListImpl(); //++

    public static void main(String[] args) {
        Supervisor supervisor = new Supervisor();
        Set channelURIs = supervisor.loadChannelList("input/bloggers.rdf");
        FeedSet feeds = supervisor.initFeeds(channelURIs);

        while (true) {
            feeds.refreshAll();
        }
    }

    public Set loadChannelList(String filename) {
        ChannelSetReader reader = new ChannelSetReader();
        return reader.load(filename);
    }

    public FeedSet initFeeds(Set channelURIs) {
        FeedSet feeds = new FeedSetImpl();
        Iterator channelIterator = channelURIs.iterator();
        FeedFetcher feedFetcher;
        Interpreter interpreter;
        String uriString;
        FormatSniffer sniffer = new FormatSniffer();
        HttpConnector connector;
        char format;
        while (channelIterator.hasNext()) {

            uriString = (String) channelIterator.next();

            //          added for Social
            connector = new HttpConnector(uriString);
            boolean streamAvailable = connector.load();
            if (streamAvailable) {
                format = sniffer.sniff(connector.getInputStream());
            } else {
                format = FeedConstants.UNKNOWN;
            }

            System.out.println(uriString + "\n"
                    + FeedConstants.formatName(format) + "\n");

            feedFetcher = new FeedFetcherImpl(uriString);
            feedFetcher.setFormatHint(format);
            interpreter = RDFInterpreterFactory.createInterpreter(format,
                    entries); // changed for Social
            feedFetcher.setInterpreter(interpreter);
            feedFetcher.setRefreshPeriod(REFRESH_PERIOD);
            feeds.addFeed(feedFetcher);
        }
        return feeds;
    }
}
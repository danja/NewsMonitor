package org.urss.social;

import java.util.Iterator;
import java.util.Set;

import org.urss.feeds.EntryList;
import org.urss.feeds.EntryListImpl;
import org.urss.feeds.FeedConstants;
import org.urss.feeds.FeedFetcher;
import org.urss.feeds.FeedFetcherImpl;
import org.urss.feeds.FeedSet;
import org.urss.feeds.FeedSetImpl;
import org.urss.io.ChannelSetReader;
import org.urss.io.HttpConnector;
import org.urss.io.Interpreter;
import org.urss.planet.FileEntrySerializer;

public class Supervisor {

    static int REFRESH_PERIOD = 10000; // milliseconds

    static int MAX_ITEMS = 5;

    static EntryList entries = new EntryListImpl(); //++

    public static void main(String[] args) {
        Supervisor supervisor = new Supervisor();
        Set channelURIs = supervisor.loadChannelList("input/feedlist.rdf");
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
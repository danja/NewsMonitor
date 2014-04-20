/*
 * Danny Ayers Oct 31, 2004 http://dannyayers.com
 *  
 */
package org.danja.feedreader.social;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.parsers.FeedParser;
import org.danja.feedreader.parsers.ParserInterpreter;
import org.danja.feedreader.parsers.Rss2Handler;
import org.danja.feedreader.parsers.SoupParser;
import org.danja.feedreader.planet.FileEntrySerializer;

public class CleanerInterpreter implements Interpreter {

    FeedParser feedParser = null;

    Rss2Handler rss2handler = null;

    EntryList entries;

    FileEntrySerializer serializer;

    public CleanerInterpreter() {
        entries = new EntryListImpl();
        initializeCleaner();
        serializer = new FileEntrySerializer();
        serializer.loadDocumentShell("input/shell.xml"); // @@TODO remove
        // hardcoding
    }

    public void interpret(FeedFetcher feed) {
        serializer.clearEntries();

        for (int i = 0; i < entries.size(); i++) {
            serializer.addEntry(entries.getEntry(i));
        }
        String filename = "data/" + RDFInterpreterFactory.getFilename(feed);
        System.out.println("\nFeed: "+feed.getURIString());
        System.out.println("type: "+ FeedConstants.formatName(feed.getFormatHint()));
        System.out.println("Writing from CleanerInterpreter...");

        serializer.transformWrite(filename, "templates/feed-rss1.0.xsl");
    }

    private void initializeCleaner() {
        FeedParser feedParser = new SoupParser();
        Interpreter interpreter = new ParserInterpreter(feedParser);
        rss2handler = new Rss2Handler();
        rss2handler.setEntryList(entries);
        feedParser.setContentHandler(rss2handler);
    }

}
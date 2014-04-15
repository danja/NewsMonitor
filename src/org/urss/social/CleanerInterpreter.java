/*
 * Danny Ayers Oct 31, 2004 http://dannyayers.com
 *  
 */
package org.urss.social;

import org.urss.feeds.EntryList;
import org.urss.feeds.EntryListImpl;
import org.urss.feeds.FeedConstants;
import org.urss.feeds.FeedFetcher;
import org.urss.io.Interpreter;
import org.urss.parsers.FeedParser;
import org.urss.parsers.ParserInterpreter;
import org.urss.parsers.Rss2Handler;
import org.urss.parsers.SoupParser;
import org.urss.planet.FileEntrySerializer;

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
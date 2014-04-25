/**
 * feedreader-prototype
 *
 * Poller.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.main;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.danja.feedreader.feeds.EntryList;
import org.danja.feedreader.feeds.EntryListImpl;
import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.feeds.FeedFetcherImpl;
import org.danja.feedreader.feeds.FeedSet;
import org.danja.feedreader.feeds.FeedSetImpl;
import org.danja.feedreader.io.FileEntrySerializer;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.io.Interpreter;
import org.danja.feedreader.parsers.InterpreterFactory;
import org.danja.feedreader.social.FormatSniffer;
import org.danja.feedreader.social.RDFInterpreterFactory;

public class Poller implements Runnable {

    private EntryList entries = new EntryListImpl(); //++

    private List<String> feedURIs = null;
    
    private FeedSet feedSet = new FeedSetImpl();

	private boolean running = false;

	private Thread thread;

    public FeedSet initFeeds() {
        FeedFetcher feedFetcher;
        Interpreter interpreter;
        String uriString;
        FormatSniffer sniffer = new FormatSniffer();
        HttpConnector connector;
        char format;
        
        System.out.println("feedURIs.size() = "+feedURIs.size());
        for(int i=0;i<feedURIs.size();i++) {
            uriString = feedURIs.get(i);
            connector = new HttpConnector(uriString);
            boolean streamAvailable = connector.load();
            if (streamAvailable) {
                format = sniffer.sniff(connector.getInputStream());
            } else {
            	System.out.println("Stream unavailable.");
                format = FeedConstants.UNKNOWN;
            }
            System.out.println("FormatNumber... : "+format);
            System.out.println("\nFeed : "+uriString);
            		System.out.println("Format : "+FeedConstants.formatName(format));
            
            feedFetcher = new FeedFetcherImpl(uriString);
            feedFetcher.setFormatHint(format);
            interpreter = RDFInterpreterFactory.createInterpreter(format,
                    entries); 
            feedFetcher.setInterpreter(interpreter);
            feedFetcher.setRefreshPeriod(Configuration.getPollerPeriod()); // TODO move
            System.out.println("feedFetcher = "+feedFetcher);
            feedSet.addFeed(feedFetcher);
        }
        return feedSet;
    }
    
    public void start(){
    	 thread = new Thread(this);
    	    thread.start();
    	System.out.println("Poller started.");
    }
    
    public void stop(){
    	running  = false;
    }
    
    @Override
    public void run() {
    	running = true;
        while (running) {
        	System.out.println("RUNNING!");
            feedSet.refreshAll();
                displayStatus(feedSet);
                System.out.println("1entries.size() = "+entries.size());
            entries.trimList(Configuration.getMaxItems());
            
            FileEntrySerializer serializer = new FileEntrySerializer();
            serializer.loadDocumentShell("input/shell.xml");
            
           serializer.clearEntries();
           System.out.println("entries.size() = "+entries.size());
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
        System.out.println("Poller stopped.");
    }
    
    public void displayStatus(FeedSet feeds) {
        Iterator feedIterator = feeds.getFeedCollection().iterator();
        while (feedIterator.hasNext()) {
            System.out.println(((FeedFetcher) feedIterator.next()).getStatus());
        }
        System.out.println("---------------");
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(entries.getEntry(i));
        }
    }

	public void setFeedList(List<String> feeds) {
		feedURIs = feeds;
	}
}
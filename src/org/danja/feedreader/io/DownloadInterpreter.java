/*
 * Danny Ayers Aug 22, 2004
 * http://dannyayers.com
 * 
 */
package org.danja.feedreader.io;

import org.danja.feedreader.feeds.FeedFetcher;
/**
 * @author danny
 *
 */
public class DownloadInterpreter  implements Interpreter{

    // CHANGED!! was extends DefaultHandler
    
    private String  downloadDir;
    
    public DownloadInterpreter(String downloadDir){
        this.downloadDir = downloadDir;
    }
    
    public void interpret(FeedFetcher feed) {
        String filename = feed.getTitle() + ".xml";
        System.out.println("SAVING:" + feed.getURIString()+"\n=>"+filename);
        feed.downloadToFile(downloadDir+filename);
    }

}

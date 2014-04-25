/**
 * TODO revisit
 * feedreader-prototype
 *
 * DonloadInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.io;

import org.danja.feedreader.feeds.Feed;
/**
 * Implementation of Interpreter, saves the data from individual feeds to file
 */
public class DownloadInterpreter  implements Interpreter{

    
    private String  downloadDir;
    
    public DownloadInterpreter(String downloadDir){
        this.downloadDir = downloadDir;
    }
    
    public void interpret(Feed feed) {
        String filename = feed.getTitle() + ".xml";
        System.out.println("SAVING:" + feed.getUrl()+"\n=>"+filename);
        feed.downloadToFile(downloadDir+filename);
    }
}

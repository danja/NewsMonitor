/*
 * Danny Ayers Nov 4, 2004
 * http://dannyayers.com
 * 
 */
package org.danja.feedreader.social;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.kowari.itql.ItqlInterpreterBean;
import org.kowari.query.QueryException;

public class KowariLoader {

    static final String DATA_DIR = "data";
    
    public static void main(String[] args) {
        URI friendsData = null; 
        try {
            friendsData = new URI(FriendlyFeeds.FRIENDS_MODEL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        ItqlInterpreterBean itql = new ItqlInterpreterBean();
        File dataDir = new File(DATA_DIR);
        File[] files;
        while(true){
        files = dataDir.listFiles();
      
        
        if(files.length > 0){
            for(int i=0;i<files.length;i++){
                try {
                    System.out.println("Loading "+files[i]);
                    itql.load(files[i], friendsData);
                    System.out.println("Deleting "+files[i]);
                   files[i].delete();
                } catch (QueryException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        }
    }
}

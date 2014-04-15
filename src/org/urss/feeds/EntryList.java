/*
 * Danny Ayers Aug 27, 2004
 * http://dannyayers.com
 * 
 */
package org.urss.feeds;

public interface EntryList {

    public void addEntry(Entry entry);
    
    public int size();

    public Entry getEntry(int i); 
    
    // added for Planet
    public void trimList(int trimSize);
}

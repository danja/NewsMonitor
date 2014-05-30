/**
 * feedreader-prototype
 *
 * EntryList.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */

package org.danja.feedreader.feeds;

import java.util.List;
import java.util.Map;


/**
 * Defines a container for an ordered series of Entry objects
 */
public interface EntryList {

    /**
     * add an entry to the list
     * 
     * @param entry Entry to add to the list
     */
    public void addEntry(Entry entry);
    
    /**
     * get the number of entries in the list
     * 
     * @return number of entries in list
     */
    public int size();

    /**
     * get an entry by it's index
     * 
     * @param index index of the required entry
     * @return
     */
    public Entry getEntry(int index); 

    /**
     * cut the list down to size
     * 
     * @param trimSize 
     */
    public void trimList(int trimSize);
    
    /**
     * Gets an RDF/Turtle representation of the EntryList (sans prefixes)
     * 
     * @return the Turtle
     */
  //  public String toTurtle();

	public void addAll(EntryList entries);

	public List<Entry> getEntries();

	public List<Map<String, Object>> getTemplateList();
}
